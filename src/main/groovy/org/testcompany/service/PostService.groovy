package org.testcompany.service

import org.springframework.stereotype.Service;
import org.testcompany.entity.Post;
import org.testcompany.exception.PostNotFoundException;
import org.testcompany.repository.PostRepository;

@Service
class PostService {

    final PostRepository postRepository

    PostService(PostRepository postRepository) {
        this.postRepository = postRepository
    }

    Post createPost(Post post) {
        try {
            return postRepository.save(post)
        } catch (Exception e) {
            throw new RuntimeException("Failed to create post: " + e.getMessage(), e)
        }
    }

    Post updatePost(String id, Post updatedPost) {
        if (postRepository.existsById(id)) {
            updatedPost.id = id
            try {
                return postRepository.save(updatedPost);
            } catch (Exception e) {
                throw new RuntimeException("Failed to update post: " + e.getMessage(), e)
            }
        } else {
            throw new PostNotFoundException("Post with ID " + id + " not found.")
        }
    }

    void deletePost(String id) {
        try {
            postRepository.deleteById(id)
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete post: " + e.getMessage(), e)
        }
    }

    Post addFavorite(String postId, String userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with ID " + postId + " not found."));
        try {
            post.favoriteByUsers.add(userId)
            return postRepository.save(post)
        } catch (Exception e) {
            throw new RuntimeException("Failed to add favorite: " + e.getMessage(), e)
        }
    }

    Post removeFavorite(String postId, String userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post with ID " + postId + " not found."));
        try {
            post.favoriteByUsers.remove(userId)
            return postRepository.save(post)
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove favorite: " + e.getMessage(), e)
        }
    }

    List<Post> getPostsByUserId(String userId) {
        try {
            return postRepository.findByUserId(userId)
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve posts by user ID: " + e.getMessage(), e)
        }
    }

    List<Post> getPostsByUserIds(List<String> userIds) {
        try {
            return postRepository.findByUserIdIn(userIds)
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve posts by user IDs: " + e.getMessage(), e)
        }
    }

    boolean existsById(String postId) {
        postRepository.existsById(postId)
    }
}