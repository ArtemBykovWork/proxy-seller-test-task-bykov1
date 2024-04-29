package org.testcompany.service

import org.springframework.stereotype.Service
import org.testcompany.entity.User
import org.testcompany.exception.InvalidUserException
import org.testcompany.exception.UserNotFoundException
import org.testcompany.repository.UserRepository

@Service
class UserService {

    final UserRepository userRepository

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    User createUser(User user) {
        try {
            if (user == null || user.id == null || user.email == null || user.name == null) {
                throw new InvalidUserException("User data is incomplete.")
            }
            return userRepository.save(user)
        } catch (Exception e) {
            throw new InvalidUserException("Failed to create user: ${e.message}")
        }
    }

    User updateUser(String id, User user) {
        if (id == null || !userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id '$id' not found.")
        }
        try {
            user.id = id
            return userRepository.save(user)
        } catch (Exception e) {
            throw new InvalidUserException("Failed to update user: ${e.message}")
        }
    }

    void deleteUser(String id) {
        checkUserExistence(id)
        try {
            userRepository.deleteById(id)
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user: ${e.message}")
        }
    }

    void checkUserExistence(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id '$id' not found.")
        }
    }
}