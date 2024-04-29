package org.testcompany.service

import org.testcompany.exception.PostNotFoundException
import spock.lang.Specification
import org.testcompany.entity.Post
import org.testcompany.repository.PostRepository

class PostServiceSpec extends Specification {

    PostRepository postRepository = Mock()
    PostService postService = new PostService(postRepository)

    def "create post successfully"() {
        given:
        Post post = new Post(id: "1", content: "Hello, World!")


        when:
        Post createdPost = postService.createPost(post)

        then:
        1 * postRepository.save(_ as Post) >> post
        createdPost != null
        createdPost.id == "1"
        createdPost.content == "Hello, World!"
    }

    def "update post throws exception when post does not exist"() {
        given:
        String postId = "1"
        Post updatedPost = new Post(id: postId, content: "Updated Content")

        when:
        postService.updatePost(postId, updatedPost)

        then:
        1 * postRepository.existsById(postId) >> false
        thrown(PostNotFoundException.class)
    }

    def "delete post successfully"() {
        given:
        String postId = "1"


        when:
        postService.deletePost(postId)

        then:
        1 * postRepository.deleteById(postId)
    }

    def "add favorite adds user to favorites list"() {
        given:
        String postId = "1"
        String userId = "user1"
        Post post = new Post(id: postId, content: "Hello, World!", favoriteByUsers: [])
        when:
        Post updatedPost = postService.addFavorite(postId, userId)

        then:
        1 * postRepository.findById(postId) >> Optional.of(post)
        1 * postRepository.save(post) >> post
        updatedPost.favoriteByUsers.contains(userId)
    }

    def "remove favorite removes user from favorites list"() {
        given:
        String postId = "1"
        String userId = "user1"
        Post post = new Post(id: postId, content: "Hello, World!", favoriteByUsers: [userId])
        postRepository.findById(postId) >> Optional.of(post)
        postRepository.save(post) >> post

        when:
        Post updatedPost = postService.removeFavorite(postId, userId)

        then:
        !updatedPost.favoriteByUsers.contains(userId)
    }

    def "get posts by user ID"() {
        given:
        String userId = "user1"
        List<Post> expectedPosts = [new Post(id: "1", userId: userId, content: "Post 1"), new Post(id: "2", userId: userId, content: "Post 2")]
        postRepository.findByUserId(userId) >> expectedPosts

        when:
        List<Post> posts = postService.getPostsByUserId(userId)

        then:
        posts.size() == 2
        posts.every { it.userId == userId }
    }

    def "get posts by multiple user IDs"() {
        given:
        List<String> userIds = ["user1", "user2"]
        List<Post> expectedPosts = [new Post(id: "1", userId: "user1", content: "Post 1"), new Post(id: "2", userId: "user2", content: "Post 2")]
        postRepository.findByUserIdIn(userIds) >> expectedPosts

        when:
        List<Post> posts = postService.getPostsByUserIds(userIds)

        then:
        posts.size() == 2
        posts.collect { it.userId } == userIds
    }
}