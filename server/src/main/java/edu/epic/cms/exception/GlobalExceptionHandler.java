package edu.epic.cms.exception;

import edu.epic.cms.api.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.error(HttpStatus.BAD_REQUEST.value(), errors));
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<CommonResponse> handleCardNotFoundException(CardNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.notFound(ex.getMessage()));
    }

    @ExceptionHandler(CardCreationException.class)
    public ResponseEntity<CommonResponse> handleCardCreationException(CardCreationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.badRequest(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateCardException.class)
    public ResponseEntity<CommonResponse> handleDuplicateCardException(DuplicateCardException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(CommonResponse.error(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(CardException.class)
    public ResponseEntity<CommonResponse> handleCardException(CardException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                        "An unexpected error occurred: " + ex.getMessage()));
    }
}
