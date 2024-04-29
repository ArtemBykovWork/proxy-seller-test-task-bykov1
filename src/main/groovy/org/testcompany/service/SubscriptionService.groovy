package org.testcompany.service


import org.springframework.stereotype.Service
import org.testcompany.entity.Subscription
import org.testcompany.exception.InvalidSubscriptionException

import org.testcompany.repository.SubscriptionRepository

@Service
class SubscriptionService {

    final SubscriptionRepository subscriptionRepository

    SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository
    }

    Subscription subscribeUser(String subscriberId, String subscribedToId) {
        if (subscriberId == null || subscribedToId == null) {
            throw new InvalidSubscriptionException("Subscriber and subscription IDs must not be null.")
        }
        if (subscriptionRepository.findBySubscriberIdAndSubscribedToId(subscriberId, subscribedToId)) {
            throw new InvalidSubscriptionException("Subscription already exists between subscriber ID $subscriberId and subscribedTo ID $subscribedToId.")
        }
        try {
            Subscription subscription = new Subscription(subscriberId: subscriberId, subscribedToId: subscribedToId)
            return subscriptionRepository.save(subscription)
        } catch (Exception e) {
            throw new RuntimeException("Failed to subscribe user: ${e.message}", e)
        }
    }

    void unsubscribeUser(String subscriberId, String subscribedToId) {
        if (subscriberId == null || subscribedToId == null) {
            throw new InvalidSubscriptionException("Subscriber and subscription IDs must not be null.")
        }
        if (!subscriptionRepository.findBySubscriberIdAndSubscribedToId(subscriberId, subscribedToId)) {
            throw new InvalidSubscriptionException("No existing subscription found between subscriber ID $subscriberId and subscribedTo ID $subscribedToId to unsubscribe.")
        }
        try {
            subscriptionRepository.deleteBySubscriberIdAndSubscribedToId(subscriberId, subscribedToId)
        } catch (Exception e) {
            throw new RuntimeException("Failed to unsubscribe user: ${e.message}", e)
        }
    }

    List<String> getSubscribedUserIds(String subscriberId) {
        if (subscriberId == null) {
            throw new InvalidSubscriptionException("Subscriber ID must not be null.")
        }
        try {
            return subscriptionRepository.findBySubscriberId(subscriberId).collect { it.subscribedToId }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve subscribed user IDs: ${e.message}", e)
        }
    }
}