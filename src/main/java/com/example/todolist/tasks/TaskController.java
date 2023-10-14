package com.example.todolist.tasks;

import com.example.todolist.Utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("task")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody Task newTask, HttpServletRequest request){
    newTask.setUserId((UUID) request.getAttribute( "userId") );

    LocalDateTime currentDate = LocalDateTime.now();

    if(currentDate.isAfter( newTask.getStartAt() )){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser maior do que a data atual.");
    }

    if(currentDate.isAfter( newTask.getEndAt() )){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de término deve ser maior do que a data atual.");
    }

    if(newTask.getStartAt().isAfter( newTask.getEndAt() ) ){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser menor do que a data de término.");
    }

    return ResponseEntity.status(HttpStatus.CREATED).body( this.taskRepository.save( newTask ) );
  }

  @GetMapping("/")
  public List<Task> getByUser(HttpServletRequest request){
    UUID userId = (UUID) request.getAttribute("userId");
    return this.taskRepository.findByUserId( userId );
  }

  @PutMapping("/{id}")
  public ResponseEntity update(@RequestBody Task newTask, HttpServletRequest request, @PathVariable UUID id){
    UUID userId = (UUID) request.getAttribute("userId");

    Task task = this.taskRepository.findById(id).orElse(null);

    if(task == null){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task não encontrada");
    }

    if(!task.getUserId().equals( userId )){
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não é possível alterar algo criado por outro usuário");
    }

    Utils.copyNonNullProperties(newTask, task);

    return ResponseEntity.status(HttpStatus.OK).body( this.taskRepository.save(task) );
  }
}
