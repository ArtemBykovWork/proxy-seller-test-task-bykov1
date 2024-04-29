package org.testcompany.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document
class Comment {
    @Id
    String id
    String postId
    String userId
    String text
    List<String> likedByUsers = []
}