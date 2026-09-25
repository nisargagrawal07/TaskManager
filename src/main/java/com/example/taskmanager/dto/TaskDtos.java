package com.example.taskmanager.dto;
import com.example.taskmanager.model.Task;
import jakarta.validation.constraints.*;
import java.time.*;
public final class TaskDtos {private TaskDtos(){}
 public record Request(@NotBlank @Size(max=150) String title,@Size(max=2000) String description,@NotNull Task.Status status,@NotNull Task.Priority priority,LocalDate dueDate){}
 public record Response(Long id,String title,String description,Task.Status status,Task.Priority priority,LocalDate dueDate,Instant createdAt,Instant updatedAt){
  public static Response from(Task t){return new Response(t.id,t.title,t.description,t.status,t.priority,t.dueDate,t.createdAt,t.updatedAt);}
 }
}
