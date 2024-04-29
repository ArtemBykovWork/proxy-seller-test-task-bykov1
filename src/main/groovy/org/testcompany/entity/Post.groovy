package org.testcompany.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Post {
    @Id
    String id
    String userId
    String content
    List<String> favoriteByUsers = []
    List<Comment> comments = []
}