package com.example.taskmanager.controller;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.*;
@RestControllerAdvice public class ApiExceptionHandler {
 record ErrorBody(Instant timestamp,int status,String error,Object details){}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ErrorBody> validation(MethodArgumentNotValidException e){Map<String,String> errors=new LinkedHashMap<>();e.getBindingResult().getFieldErrors().forEach(f->errors.put(f.getField(),f.getDefaultMessage()));return ResponseEntity.badRequest().body(new ErrorBody(Instant.now(),400,"Validation failed",errors));}
 @ExceptionHandler({HttpMessageNotReadableException.class,MethodArgumentTypeMismatchException.class}) ResponseEntity<ErrorBody> malformed(Exception e){return ResponseEntity.badRequest().body(new ErrorBody(Instant.now(),400,"Invalid request",null));}
 @ExceptionHandler(ResponseStatusException.class) ResponseEntity<ErrorBody> status(ResponseStatusException e){return ResponseEntity.status(e.getStatusCode()).body(new ErrorBody(Instant.now(),e.getStatusCode().value(),e.getReason(),null));}
}
