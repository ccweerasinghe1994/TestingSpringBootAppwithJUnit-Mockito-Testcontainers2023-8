package com.wchamara.springboottesting.exception;

/**
 * This class extends the RuntimeException class.
 * It is used to indicate that a resource was not found.
 * It can be thrown when trying to find a resource with a specific id or other unique identifier, and the resource does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor for the ResourceNotFoundException.
     * It takes a message as a parameter and passes it to the superclass constructor.
     * The message should provide information about the resource that was not found.
     *
     * @param message The message about the resource that was not found.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor for the ResourceNotFoundException.
     * It takes a message and a cause as parameters and passes them to the superclass constructor.
     * The message should provide information about the resource that was not found.
     * The cause is the underlying exception that caused this exception to be thrown.
     *
     * @param message The message about the resource that was not found.
     * @param cause   The underlying exception that caused this exception to be thrown.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}