package org.testcompany.controller

import org.springframework.web.bind.annotation.*
import org.testcompany.entity.Post
import org.testcompany.service.PostService

@RestController
@RequestMapping("/posts")
class PostController {

    final PostService postService

    PostController(PostService postService) {
        this.postService = postService
    }

    @PostMapping
    Post createPost(@RequestBody Post post) {
        return postService.createPost(post)
    }

    @PutMapping("/{id}")
    Post updatePost(@PathVariable("id") String id, @RequestBody Post updatedPost) {
        return postService.updatePost(id, updatedPost)
    }

    @DeleteMapping("/{id}")
    void deletePost(@PathVariable("id") String id) {
        postService.deletePost(id)
    }

    @PostMapping("/{postId}/favorites/{userId}")
    Post addFavorite(@PathVariable("postId") String postId, @PathVariable("userId") String userId) {
        return postService.addFavorite(postId, userId)
    }

    @DeleteMapping("/{postId}/favorites/{userId}")
    Post removeFavorite(@PathVariable("postId") String postId, @PathVariable("userId") String userId) {
        return postService.removeFavorite(postId, userId)
    }
}