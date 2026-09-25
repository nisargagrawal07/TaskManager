package com.example.taskmanager.model;
import jakarta.persistence.*;
import java.time.*;
@Entity @Table(name="tasks") public class Task {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,length=150) public String title;
 @Column(length=2000) public String description;
 @Enumerated(EnumType.STRING) @Column(nullable=false) public Status status=Status.TODO;
 @Enumerated(EnumType.STRING) @Column(nullable=false) public Priority priority=Priority.MEDIUM;
 @Column(name="due_date") public LocalDate dueDate;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="owner_id") public AppUser owner;
 @Column(name="created_at",nullable=false) public Instant createdAt=Instant.now();
 @Column(name="updated_at",nullable=false) public Instant updatedAt=Instant.now();
 @PreUpdate void touch(){updatedAt=Instant.now();}
 protected Task(){} public Task(String title,String description,Status status,Priority priority,LocalDate dueDate,AppUser owner){this.title=title;this.description=description;this.status=status;this.priority=priority;this.dueDate=dueDate;this.owner=owner;}
 public enum Status {TODO,IN_PROGRESS,DONE} public enum Priority {LOW,MEDIUM,HIGH}
}
