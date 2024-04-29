package org.testcompany.exception

class FeedServiceException extends RuntimeException {
    FeedServiceException(String message, Throwable cause) {
        super(message, cause)
    }
}