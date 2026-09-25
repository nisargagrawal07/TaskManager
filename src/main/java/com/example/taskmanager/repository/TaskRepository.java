package com.example.taskmanager.repository;
import com.example.taskmanager.model.Task;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TaskRepository extends JpaRepository<Task,Long>{
 Page<Task> findByOwnerId(Long ownerId,Pageable pageable);
 Page<Task> findByOwnerIdAndStatus(Long ownerId,Task.Status status,Pageable pageable);
 Optional<Task> findByIdAndOwnerId(Long id,Long ownerId);
}
