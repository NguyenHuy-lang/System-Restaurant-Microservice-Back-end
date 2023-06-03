package com.micro.app.controller;
import com.micro.app.model.User;
import com.micro.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
public class AuthenticationController {
    private final UserRepository userRepository;
    private final AuthenticationService service;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/auth/infor")
    public ResponseEntity<User> infor(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = userRepository.findByEmail(email);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable("user_id") Integer userId) {
        User user = userRepository.findById(userId).get();
        return ResponseEntity.ok().body(user);
    }


}