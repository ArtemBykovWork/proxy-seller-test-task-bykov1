package org.testcompany.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.testcompany.entity.Comment

interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(String postId)
}