package com.example.taskmanager.model;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="app_users") public class AppUser {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,length=100) public String name;
 @Column(nullable=false,unique=true) public String email;
 @Column(nullable=false) public String password;
 @Column(name="created_at",nullable=false) public Instant createdAt=Instant.now();
 protected AppUser() {} public AppUser(String name,String email,String password){this.name=name;this.email=email;this.password=password;}
}
