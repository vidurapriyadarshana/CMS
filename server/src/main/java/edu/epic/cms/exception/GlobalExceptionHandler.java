package edu.epic.cms.exception;

import edu.epic.cms.util.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
