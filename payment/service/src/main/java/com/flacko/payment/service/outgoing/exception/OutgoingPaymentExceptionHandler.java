package com.flacko.payment.service.outgoing.exception;

import com.flacko.common.exception.NotFoundException;
import com.flacko.common.exception.OutgoingPaymentIllegalStateTransitionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class OutgoingPaymentExceptionHandler {

    @ExceptionHandler(OutgoingPaymentIllegalStateTransitionException.class)
    public ResponseEntity<String> handlePaymentIllegalStateTransitionException(
            OutgoingPaymentIllegalStateTransitionException e) {
        log.error("Unexpected error occurred", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        log.error("Unexpected error occurred", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Unexpected error occurred", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
