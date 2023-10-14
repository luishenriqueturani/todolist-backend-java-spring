package com.example.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private IUserRepository userRepository;
  @PostMapping("/new")
  public ResponseEntity create(@RequestBody User user){
    User u = this.userRepository.findByUserName(user.getUserName());
    if(u != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User name já existe");
    }

    user.setPassword(BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray()));

    return ResponseEntity.status(HttpStatus.CREATED).body( this.userRepository.save( user ) );
  }
}
