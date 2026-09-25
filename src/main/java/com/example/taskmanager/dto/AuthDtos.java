package com.example.taskmanager.dto;
import jakarta.validation.constraints.*;
public final class AuthDtos {private AuthDtos(){}
 public record Register(@NotBlank String name,@Email @NotBlank String email,@Size(min=8,max=72) String password){}
 public record Login(@Email @NotBlank String email,@NotBlank String password){}
 public record Token(String token,String tokenType){}
}
