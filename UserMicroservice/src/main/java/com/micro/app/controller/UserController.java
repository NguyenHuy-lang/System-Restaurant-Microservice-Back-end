package com.micro.app.controller;

import com.micro.app.model.User;
import com.micro.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    @PostMapping
    ResponseEntity<User> signIn(@RequestBody User user) {
        user = userRepository.checkLogin(user.getPassword(), user.getUsername());
        if(user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(user);
        }
    }
}
