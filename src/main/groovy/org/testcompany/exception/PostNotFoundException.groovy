package org.testcompany.exception

class PostNotFoundException extends RuntimeException {
    PostNotFoundException(String message) {
        super(message)
    }
}