package com.example.todolist.tasks;

import com.example.todolist.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tb_tasks")
public class Task {

  private UUID userId;

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private String decription;

  @Column(length = 50)
  private String title;

  private LocalDateTime startAt;

  private LocalDateTime endAt;

  private String priority;

  @CreationTimestamp
  private LocalDateTime createdAt;

}
