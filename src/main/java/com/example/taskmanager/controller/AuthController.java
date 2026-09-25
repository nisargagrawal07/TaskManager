package com.example.taskmanager.controller;
import com.example.taskmanager.dto.AuthDtos;
import com.example.taskmanager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") public class AuthController {
 private final AuthService service;public AuthController(AuthService service){this.service=service;}
 @PostMapping("/register") public ResponseEntity<AuthDtos.Token> register(@Valid @RequestBody AuthDtos.Register r){return ResponseEntity.status(HttpStatus.CREATED).body(service.register(r));}
 @PostMapping("/login") public AuthDtos.Token login(@Valid @RequestBody AuthDtos.Login r){return service.login(r);}
}
