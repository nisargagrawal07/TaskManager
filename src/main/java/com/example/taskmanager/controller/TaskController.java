package com.example.taskmanager.controller;
import com.example.taskmanager.dto.TaskDtos;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/tasks") public class TaskController {
 private final TaskService service;public TaskController(TaskService service){this.service=service;}
 @PostMapping public ResponseEntity<TaskDtos.Response> create(@AuthenticationPrincipal Long uid,@Valid @RequestBody TaskDtos.Request r){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(uid,r));}
 @GetMapping public Page<TaskDtos.Response> list(@AuthenticationPrincipal Long uid,@RequestParam(required=false) Task.Status status,@PageableDefault(size=20,sort="createdAt",direction=Sort.Direction.DESC) Pageable pageable){return service.list(uid,status,pageable);}
 @GetMapping("/{id}") public TaskDtos.Response get(@AuthenticationPrincipal Long uid,@PathVariable Long id){return service.get(uid,id);}
 @PutMapping("/{id}") public TaskDtos.Response update(@AuthenticationPrincipal Long uid,@PathVariable Long id,@Valid @RequestBody TaskDtos.Request r){return service.update(uid,id,r);}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@AuthenticationPrincipal Long uid,@PathVariable Long id){service.delete(uid,id);}
}
