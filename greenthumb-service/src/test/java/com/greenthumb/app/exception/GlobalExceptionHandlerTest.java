package com.greenthumb.app.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();


    @Test
    void handleResourceNotFound_ShouldReturnNotFoundStatus() {

        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");


        ResponseEntity<Map<String, String>> response = exceptionHandler.handleResourceNotFound(ex);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody().get("message"));
    }


    @Test
    void handleUserAlreadyExistsException_ShouldReturnConflictStatus() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("User exists");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleUserAlreadyExistsException(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User exists", response.getBody().get("message"));
    }


    @Test
    void handleBusinessException_ShouldReturnBadRequestStatus() {
        BusinessException ex = new BusinessException("Business error");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleBusinessException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Business error", response.getBody().get("message"));
    }


    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        Exception ex = new Exception("Unexpected error");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleGenericException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: Unexpected error", response.getBody().get("message"));
    }
}
