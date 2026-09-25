package com.example.taskmanager.service;
import com.example.taskmanager.dto.TaskDtos;
import com.example.taskmanager.model.*;
import com.example.taskmanager.repository.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
@Service public class TaskService {
 private final TaskRepository tasks;private final UserRepository users;
 public TaskService(TaskRepository tasks,UserRepository users){this.tasks=tasks;this.users=users;}
 @Transactional public TaskDtos.Response create(Long uid,TaskDtos.Request r){AppUser owner=users.findById(uid).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED));return TaskDtos.Response.from(tasks.save(new Task(r.title().trim(),r.description(),r.status(),r.priority(),r.dueDate(),owner)));}
 @Transactional(readOnly=true) public Page<TaskDtos.Response> list(Long uid,Task.Status status,Pageable pageable){return (status==null?tasks.findByOwnerId(uid,pageable):tasks.findByOwnerIdAndStatus(uid,status,pageable)).map(TaskDtos.Response::from);}
 @Transactional(readOnly=true) public TaskDtos.Response get(Long uid,Long id){return TaskDtos.Response.from(owned(uid,id));}
 @Transactional public TaskDtos.Response update(Long uid,Long id,TaskDtos.Request r){Task t=owned(uid,id);t.title=r.title().trim();t.description=r.description();t.status=r.status();t.priority=r.priority();t.dueDate=r.dueDate();return TaskDtos.Response.from(tasks.saveAndFlush(t));}
 @Transactional public void delete(Long uid,Long id){tasks.delete(owned(uid,id));}
 private Task owned(Long uid,Long id){return tasks.findByIdAndOwnerId(id,uid).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found"));}
}
