package org.testcompany.controller

import org.springframework.web.bind.annotation.*
import org.testcompany.entity.Comment
import org.testcompany.service.CommentService

@RestController
@RequestMapping("/comments")
class CommentController {

    final CommentService commentService

    CommentController(CommentService commentService) {
        this.commentService = commentService
    }

    @PostMapping
    Comment addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment)
    }

    @GetMapping("/post/{postId}")
    List<Comment> getComments(@PathVariable("postId") String postId) {
        return commentService.getCommentsByPostId(postId)
    }

    @PostMapping('/{commentId}/like')
    Comment likeComment(@PathVariable("commentId") String commentId, @RequestBody String userId) {
        return commentService.likeComment(commentId, userId)
    }

    @PostMapping('/{commentId}/unlike')
    Comment unlikeComment(@PathVariable("commentId") String commentId, @RequestBody String userId) {
        return commentService.unlikeComment(commentId, userId)
    }
}