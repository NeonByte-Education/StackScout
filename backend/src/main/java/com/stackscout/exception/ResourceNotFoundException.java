// File: ResourceNotFoundException.java
package com.stackscout.exception;

/**
 * Исключение, которое выбрасывается, когда запрашиваемый ресурс (например,
 * библиотека или лицензия) не найден в системе.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
