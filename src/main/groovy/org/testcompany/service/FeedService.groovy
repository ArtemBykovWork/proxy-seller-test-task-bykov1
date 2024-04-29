package org.testcompany.service
import org.springframework.stereotype.Service
import org.testcompany.entity.Post
import org.testcompany.exception.FeedServiceException

import java.util.stream.Collectors

@Service
class FeedService {
    private final PostService postService
    private final CommentService commentService
    private final UserService userService
    private final SubscriptionService subscriptionService

    FeedService(PostService postService, CommentService commentService, UserService userService, SubscriptionService subscriptionService) {
        this.postService = postService
        this.commentService = commentService
        this.userService = userService
        this.subscriptionService = subscriptionService
    }

    List<Post> getUserFeed(String userId) {
        userService.checkUserExistence(userId)
        try {
            List<String> subscribedToIds = subscriptionService.getSubscribedUserIds(userId)
            List<Post> posts = postService.getPostsByUserIds(subscribedToIds)
            posts.forEach(post -> {
                try {
                    post.comments = commentService.getCommentsByPostId(post.id)
                } catch (Exception e) {
                    throw new FeedServiceException("Failed to load comments for post ID " + post.id, e)
                }
            });
            return posts
        } catch (Exception e) {
            throw new FeedServiceException("Failed to generate feed for user ID " + userId, e)
        }
    }

    List<Post> getAnotherUserFeed(String userId) {
        userService.checkUserExistence(userId)
        try {
            return postService.getPostsByUserId(userId).stream().peek(post -> {
                try {
                    post.comments = commentService.getCommentsByPostId(post.id)
                } catch (Exception e) {
                    throw new FeedServiceException("Failed to load comments for post ID " + post.id, e);
                }
            }).collect(Collectors.toList())
        } catch (Exception e) {
            throw new FeedServiceException("Failed to retrieve feed for user ID " + userId, e)
        }
    }
}
