package org.testcompany.service

import org.testcompany.exception.PostNotFoundException
import spock.lang.Specification
import org.testcompany.entity.Comment
import org.testcompany.repository.CommentRepository
import org.testcompany.exception.CommentNotFoundException

class CommentServiceSpec extends Specification {
    CommentRepository commentRepository = Mock()
    PostService postService = Mock();
    CommentService commentService = new CommentService(commentRepository, postService)

    def "addComment should save comment successfully"() {
        given:
        Comment comment = new Comment(text: "This is a test comment", postId: "123")
        postService.existsById("123") >> true

        when:
        Comment savedComment = commentService.addComment(comment)

        then:
        1 * commentRepository.save(comment) >> comment
        savedComment == comment
    }

    def "addComment should throw IllegalArgumentException if comment is null"() {
        when:
        commentService.addComment(null)

        then:
        thrown(IllegalArgumentException)
        0 * commentRepository.save(_)
    }

    def "getCommentsByPostId should return comments when found"() {
        given:
        String postId = "123"
        List<Comment> comments = [new Comment(text: "Sample comment", postId: postId)]

        when:
        List<Comment> result = commentService.getCommentsByPostId(postId)

        then:
        1 * commentRepository.findByPostId(postId) >> comments
        assert result == comments
    }

    def "getCommentsByPostId should throw IllegalArgumentException if postId is null"() {
        when:
        commentService.getCommentsByPostId(null)

        then:
        thrown(IllegalArgumentException)
    }

    def "getCommentsByPostId should throw IllegalArgumentException if postId is empty"() {
        when:
        commentService.getCommentsByPostId("")

        then:
        thrown(IllegalArgumentException)
    }

    def "getCommentsByPostId should throw CommentNotFoundException if no comments found"() {
        given:
        String postId = "unknown"
        commentRepository.findByPostId(postId) >> []

        when:
        commentService.getCommentsByPostId(postId)

        then:
        thrown(CommentNotFoundException)
        1 * commentRepository.findByPostId(postId)
    }

    def "throw PostNotFoundException when adding a comment to a non-existent post"() {
        given:
        String nonExistentPostId = "non-existent-post-id"
        Comment comment = new Comment(postId: nonExistentPostId, userId: "user-id", text: "Sample comment")

        postService.existsById(nonExistentPostId) >> false

        when:
        commentService.addComment(comment)

        then:
        thrown(PostNotFoundException)
    }

    def "like comment adds user ID to likedByUsers list and saves the comment"() {
        given:
        String commentId = "123"
        String userId = "user1"
        Comment existingComment = new Comment(id: commentId, likedByUsers: [])

        when:
        Comment result = commentService.likeComment(commentId, userId)

        then:
        1 * commentRepository.findById(commentId) >> Optional.of(existingComment)
        1 * commentRepository.save(_) >> { Comment c -> c }
        result.likedByUsers.contains(userId)
    }

    def "unlike comment removes user ID from likedByUsers list and saves the comment"() {
        given:
        String commentId = "123"
        String userId = "user1"
        Comment existingComment = new Comment(id: commentId, likedByUsers: [userId])

        when:
        Comment result = commentService.unlikeComment(commentId, userId)

        then:
        1 * commentRepository.findById(commentId) >> Optional.of(existingComment)
        1 * commentRepository.save(_) >> { Comment c -> c }
        !result.likedByUsers.contains(userId)
    }

    def "trying to like a non-existing comment throws RuntimeException"() {
        given:
        String commentId = "999"
        String userId = "user1"
        commentRepository.findById(commentId) >> Optional.empty()

        when:
        commentService.likeComment(commentId, userId)

        then:
        thrown(CommentNotFoundException)
    }

    def "trying to unlike a non-existing comment throws RuntimeException"() {
        given:
        String commentId = "999"
        String userId = "user1"
        commentRepository.findById(commentId) >> Optional.empty()

        when:
        commentService.unlikeComment(commentId, userId)

        then:
        thrown(CommentNotFoundException)
    }
}