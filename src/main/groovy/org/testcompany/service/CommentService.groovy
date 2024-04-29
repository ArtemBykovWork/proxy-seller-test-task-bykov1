package org.testcompany.service

import io.micrometer.common.util.StringUtils
import org.springframework.stereotype.Service
import org.testcompany.entity.Comment
import org.testcompany.exception.PostNotFoundException
import org.testcompany.repository.CommentRepository
import org.testcompany.exception.CommentNotFoundException

@Service
class CommentService {

    final CommentRepository commentRepository
    final PostService postService

    CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository
        this.postService = postService
    }

    Comment addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null")
        }
        if (!postService.existsById(comment.postId)) {
            throw new PostNotFoundException("Post with ID: ${comment.postId} does not exist")
        }
        try {
            return commentRepository.save(comment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save the comment: " + e.getMessage())
        }
    }

    List<Comment> getCommentsByPostId(String postId) {
        if (postId == null || StringUtils.isEmpty(postId)) {
            throw new IllegalArgumentException("Post ID must not be null or empty")
        }
        List<Comment> comments = commentRepository.findByPostId(postId)
        if (comments == null || comments.isEmpty()) {
            throw new CommentNotFoundException("No comments found for post ID: " + postId)
        }
        return comments;
    }

    Comment likeComment(String commentId, String userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow {
            new CommentNotFoundException("Comment " + commentId + " not found")
        }
        if (!comment.likedByUsers.contains(userId)) {
            comment.likedByUsers << userId
            commentRepository.save(comment)
        }
        return comment
    }

    Comment unlikeComment(String commentId, String userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow {
            new CommentNotFoundException("Comment " + commentId + " not found")
        }
        comment.likedByUsers.remove(userId)
        commentRepository.save(comment)
        return comment
    }
}