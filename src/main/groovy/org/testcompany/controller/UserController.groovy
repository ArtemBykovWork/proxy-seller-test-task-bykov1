package org.testcompany.controller


import org.springframework.web.bind.annotation.*
import org.testcompany.entity.User
import org.testcompany.service.UserService

@RestController
@RequestMapping("/users")
class UserController {

    final UserService userService

    UserController(UserService userService) {
        this.userService = userService
    }

    @PostMapping
    User createUser(@RequestBody User user) {
        return userService.createUser(user)
    }

    @PutMapping("/{id}")
    User updateUser(@PathVariable("id") String id, @RequestBody User user) {
        return userService.updateUser(id, user)
    }

    @DeleteMapping("/{id}")
    void deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id)
    }
}