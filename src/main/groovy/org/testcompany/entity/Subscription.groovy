package org.testcompany.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document
class Subscription {
    @Id
    String id
    String subscriberId
    String subscribedToId
}