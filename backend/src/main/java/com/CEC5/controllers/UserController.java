package com.CEC5.controllers;

import com.CEC5.emails.EmailService;
import com.CEC5.entity.User;
import com.CEC5.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @GetMapping
    public User getUser(@NotEmpty @RequestBody String email) {
        LOGGER.info(email);
        return userService.findUser(email);
    }

    @GetMapping("/login")
    public void login(@NotEmpty @RequestBody JsonNode requestBody) {
        User u = userService.findUser(requestBody.get("email").asText());
        if (u == null || !u.getPassword().equals(requestBody.get("password").asText()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password");
    }

    @PostMapping("/newUser")
    public User createNewUser(@Valid @RequestBody User user) {
        if (userService.isEmailPresent(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        User u = userService.saveUser(user);
        emailService.newUserCreated(u);
        return u;
    }
}
