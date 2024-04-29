package org.testcompany.service

import spock.lang.Specification
import org.testcompany.repository.SubscriptionRepository
import org.testcompany.entity.Subscription
import org.testcompany.exception.InvalidSubscriptionException

class SubscriptionServiceSpec extends Specification {

    SubscriptionRepository subscriptionRepository = Mock(SubscriptionRepository)
    SubscriptionService subscriptionService = new SubscriptionService(subscriptionRepository)

    def "subscribe user with valid input"() {
        given:
        String subscriberId = 'sub1'
        String subscribedToId = 'sub2'
        Subscription subscription = new Subscription(subscriberId: subscriberId, subscribedToId: subscribedToId)
        subscriptionRepository.save(_) >> subscription

        when:
        Subscription result = subscriptionService.subscribeUser(subscriberId, subscribedToId)

        then:
        1 * subscriptionRepository.save(_) >> subscription
        assert result == subscription
    }

    def "subscribe user with null subscriber ID"() {
        when:
        subscriptionService.subscribeUser(null, 'sub2')

        then:
        thrown(InvalidSubscriptionException)
    }

    def "subscribe user with null subscribed to ID"() {
        when:
        subscriptionService.subscribeUser('sub1', null)

        then:
        thrown(InvalidSubscriptionException)
    }

    def "unsubscribe user with valid input"() {
        given:
        String subscriberId = 'sub1'
        String subscribedToId = 'sub2'

        when:
        subscriptionService.unsubscribeUser(subscriberId, subscribedToId)

        then:
        1 * subscriptionRepository.findBySubscriberIdAndSubscribedToId(subscriberId, subscribedToId) >> new Subscription()
        1 * subscriptionRepository.deleteBySubscriberIdAndSubscribedToId(subscriberId, subscribedToId)
        noExceptionThrown()
    }

    def "unsubscribe user with null subscriber ID"() {
        when:
        subscriptionService.unsubscribeUser(null, 'sub2')

        then:
        thrown(InvalidSubscriptionException)
    }

    def "unsubscribe user with null subscribed to ID"() {
        when:
        subscriptionService.unsubscribeUser('sub1', null)

        then:
        thrown(InvalidSubscriptionException)
    }

    def "get subscribed user IDs with valid subscriber ID"() {
        given:
        String subscriberId = 'sub1'
        List<Subscription> subscriptions = [new Subscription(subscriberId: 'sub1', subscribedToId: 'user1')]
        subscriptionRepository.findBySubscriberId(subscriberId) >> subscriptions

        when:
        List<String> result = subscriptionService.getSubscribedUserIds(subscriberId)

        then:
        1 * subscriptionRepository.findBySubscriberId(subscriberId) >> subscriptions
        assert result == subscriptions*.subscribedToId
    }

    def "get subscribed user IDs with null subscriber ID"() {
        when:
        subscriptionService.getSubscribedUserIds(null)

        then:
        thrown(InvalidSubscriptionException)
    }
}