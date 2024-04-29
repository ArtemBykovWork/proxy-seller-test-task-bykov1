package org.testcompany.service

import spock.lang.Specification
import org.testcompany.entity.Post
import org.testcompany.entity.Comment
import org.testcompany.exception.UserNotFoundException

class FeedServiceSpec extends Specification {

    PostService postService = Mock()
    CommentService commentService = Mock()
    UserService userService = Mock()
    SubscriptionService subscriptionService = Mock()

    FeedService feedService = new FeedService(postService, commentService, userService, subscriptionService)

    def "getUserFeed should return user feed if user exists and has subscriptions"() {
        given: "An existing user id and subscriptions"
        String userId = "user123"
        List<String> subscribedToIds = ["user456", "user789"]
        List<Post> posts = [new Post(id: "post1"), new Post(id: "post2")]
        posts.each { post -> post.comments = [new Comment(postId: post.id, text: "Sample comment")] }

        userService.checkUserExistence(userId) >> { }
        subscriptionService.getSubscribedUserIds(userId) >> subscribedToIds
        postService.getPostsByUserIds(subscribedToIds) >> posts
        commentService.getCommentsByPostId(_ as String) >> { String postId -> [new Comment(postId: postId, text: "Sample comment")] }

        when: "getUserFeed is called"
        List<Post> result = feedService.getUserFeed(userId)

        then: "The correct posts are returned with comments"
        result == posts
        result*.comments*.text.flatten() == ["Sample comment", "Sample comment"]
    }

    def "getUserFeed should throw UserNotFoundException if user does not exist"() {
        given: "A non-existing user id"
        String userId = "userXYZ"

        userService.checkUserExistence(userId) >> { throw new UserNotFoundException("User with id '$userId' not found.") }

        when: "getUserFeed is called with a non-existing user"
        feedService.getUserFeed(userId)

        then: "UserNotFoundException is thrown"
        thrown(UserNotFoundException)
    }

    def "getAnotherUserFeed should return posts for an existing user"() {
        given: "An existing user id"
        String userId = "user123"
        List<Post> posts = [new Post(id: "post1", userId: userId), new Post(id: "post2", userId: userId)]
        posts.each { post -> post.comments = [new Comment(postId: post.id, text: "Another comment")] }

        userService.checkUserExistence(userId) >> { }
        postService.getPostsByUserId(userId) >> posts
        commentService.getCommentsByPostId(_ as String) >> { String postId -> [new Comment(postId: "post2", text: "Another comment")] }

        when: "getAnotherUserFeed is called"
        List<Post> result = feedService.getAnotherUserFeed(userId)

        then: "The correct posts are returned with comments"
        result == posts
        result*.comments*.text.flatten() == ["Another comment", "Another comment"]
    }

    def "getAnotherUserFeed should throw UserNotFoundException if user does not exist"() {
        given: "A non-existing user id"
        String userId = "userXYZ"

        userService.checkUserExistence(userId) >> { throw new UserNotFoundException("User with id '$userId' not found.") }

        when: "getAnotherUserFeed is called with a non-existing user"
        feedService.getAnotherUserFeed(userId)

        then: "UserNotFoundException is thrown"
        thrown(UserNotFoundException)
    }
}