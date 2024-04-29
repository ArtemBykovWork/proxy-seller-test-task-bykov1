package org.testcompany.controller


import org.springframework.web.bind.annotation.*
import org.testcompany.entity.Post
import org.testcompany.service.FeedService

@RestController
@RequestMapping("/feeds")
class FeedController {

    final FeedService feedService

    FeedController(FeedService feedService) {
        this.feedService = feedService
    }

    @GetMapping("/user/{userId}")
    List<Post> getUserFeed(@PathVariable("userId") String userId) {
        return feedService.getUserFeed(userId)
    }

    @GetMapping("/user/{userId}/other")
    List<Post> getAnotherUserFeed(@PathVariable("userId") String userId) {
        return feedService.getAnotherUserFeed(userId)
    }
}