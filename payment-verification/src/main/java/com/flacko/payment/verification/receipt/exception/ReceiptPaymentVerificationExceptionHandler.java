package com.flacko.payment.verification.receipt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReceiptPaymentVerificationExceptionHandler {

    @ExceptionHandler(ReceiptPaymentVerificationNotFoundException.class)
    public ResponseEntity<String> handleReceiptPaymentVerificationNotFoundException(
            ReceiptPaymentVerificationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
