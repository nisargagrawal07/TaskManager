package com.example.taskmanager.service;
import com.example.taskmanager.dto.AuthDtos;
import com.example.taskmanager.model.AppUser;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Locale;
@Service public class AuthService {
 private final UserRepository users;private final PasswordEncoder encoder;private final JwtService jwt;
 public AuthService(UserRepository users,PasswordEncoder encoder,JwtService jwt){this.users=users;this.encoder=encoder;this.jwt=jwt;}
 @Transactional public AuthDtos.Token register(AuthDtos.Register r){String email=r.email().trim().toLowerCase(Locale.ROOT);
  if(users.existsByEmail(email))throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");
  try {users.saveAndFlush(new AppUser(r.name().trim(),email,encoder.encode(r.password())));}
  catch(DataIntegrityViolationException e){throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");}
  return new AuthDtos.Token(jwt.create(email),"Bearer");
 }
 public AuthDtos.Token login(AuthDtos.Login r){String email=r.email().trim().toLowerCase(Locale.ROOT);
  AppUser user=users.findByEmail(email).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials"));
  if(!encoder.matches(r.password(),user.password))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid credentials");
  return new AuthDtos.Token(jwt.create(email),"Bearer");
 }
}
