package org.testcompany.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.testcompany.entity.Subscription

interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    List<Subscription> findBySubscriberId(String subscriberId)
    void deleteBySubscriberIdAndSubscribedToId(String subscriberId, String subscribedToId)
    @Query("{ 'subscriberId' : ?0, 'subscribedToId' : ?1 }")
    Subscription findBySubscriberIdAndSubscribedToId(String subscriberId, String subscribedToId);
}