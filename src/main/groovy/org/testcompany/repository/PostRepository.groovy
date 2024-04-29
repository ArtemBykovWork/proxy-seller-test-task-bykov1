package org.testcompany.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.testcompany.entity.Post

interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserId(String userId)
    List<Post> findByUserIdIn(List<String> userIds)
}