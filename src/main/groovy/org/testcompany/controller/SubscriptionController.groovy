package org.testcompany.controller

import org.springframework.web.bind.annotation.*
import org.testcompany.entity.Subscription
import org.testcompany.service.SubscriptionService

@RestController
@RequestMapping("/subscriptions")
class SubscriptionController {

    final SubscriptionService subscriptionService

    SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService
    }

    @PostMapping
    Subscription subscribe(@RequestParam("subscriberId") String subscriberId, @RequestParam("subscribedToId") String subscribedToId) {
        return subscriptionService.subscribeUser(subscriberId, subscribedToId)
    }

    @DeleteMapping
    void unsubscribe(@RequestParam("subscriberId") String subscriberId, @RequestParam("subscribedToId") String subscribedToId) {
        subscriptionService.unsubscribeUser(subscriberId, subscribedToId)
    }
}