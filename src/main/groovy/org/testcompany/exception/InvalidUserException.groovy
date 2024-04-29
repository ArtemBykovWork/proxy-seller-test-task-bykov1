package org.testcompany.exception;

class InvalidUserException extends RuntimeException {
    InvalidUserException(String message) {
        super(message)
    }
}