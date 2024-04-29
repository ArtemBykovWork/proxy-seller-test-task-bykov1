package org.testcompany.service

import org.testcompany.repository.UserRepository
import spock.lang.Specification
import org.testcompany.entity.User
import org.testcompany.exception.InvalidUserException
import org.testcompany.exception.UserNotFoundException

class UserServiceSpec extends Specification {
    UserRepository userRepository = Mock()
    UserService userService = new UserService(userRepository)

    def "create user with valid data"() {
        given: "valid user data"
        User validUser = new User(id: '1', name: "John Doe", email: "john@example.com")

        when: "createUser is called"
        User result = userService.createUser(validUser)

        then: "user is saved successfully"
        1 * userRepository.save(validUser) >> validUser
        result == validUser
    }

    def "fail to create user with incomplete data"() {
        given: "incomplete user data"
        User incompleteUser = new User(id: '1', name: "John Doe", email: null)

        when: "createUser is called"
        userService.createUser(incompleteUser)

        then: "an exception is thrown"
        thrown(InvalidUserException)
    }

    def "update existing user with valid data"() {
        given: "a valid user ID and updated user data"
        String id = "1"
        User updatedUser = new User(id: id, name: "Jane Doe", email: "jane@example.com")

        when: "updateUser method is called"
        User result = userService.updateUser(id, updatedUser)

        then: "the user is updated successfully"
        1 * userRepository.existsById(id) >> true
        1 * userRepository.save(updatedUser) >> updatedUser
        result == updatedUser
    }

    def "update user with non-existing id"() {
        given: "a non-existing user ID"
        String id = "99"
        User updatedUser = new User(id: id, name: "Jane Doe", email: "jane@example.com")
        userRepository.existsById(id) >> false

        when: "updateUser method is called"
        userService.updateUser(id, updatedUser)

        then: "UserNotFoundException is thrown"
        thrown(UserNotFoundException)
    }

    def "delete existing user"() {
        given: "an existing user ID"
        String id = "1"

        when: "deleteUser is called"
        userService.deleteUser(id)

        then: "user is deleted successfully"
        1 * userRepository.existsById(id) >> true
        1 * userRepository.deleteById(id)
    }

    def "delete non-existing user"() {
        given: "a non-existing user ID"
        String id = "99"
        userRepository.existsById(id) >> false

        when: "deleteUser is called"
        userService.deleteUser(id)

        then: "UserNotFoundException is thrown"
        thrown(UserNotFoundException)
    }
}