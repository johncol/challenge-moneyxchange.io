package com.belatrix.test.moneyxchangeapi.handler;

import com.belatrix.test.moneyxchangeapi.exception.ValutaNotSupportedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RatesExceptionHandler {

  @ExceptionHandler(ValutaNotSupportedException.class)
  public ResponseEntity<?> handle(ValutaNotSupportedException exception) {
    log.error("Valuta not supported", exception);
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(exception.getMessage());
  }

}
