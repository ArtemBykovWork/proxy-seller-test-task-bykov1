package org.testcompany.exception

class InvalidSubscriptionException extends RuntimeException {
    InvalidSubscriptionException(String message) {
        super(message)
    }
}