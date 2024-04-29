package org.testcompany.exception

class CommentNotFoundException extends RuntimeException {
    CommentNotFoundException(String message) {
        super(message)
    }
}