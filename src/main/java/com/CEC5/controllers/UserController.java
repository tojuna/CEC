package com.CEC5.controllers;

import com.CEC5.entity.User;
import com.CEC5.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @PostMapping("/newUser")
    public User createNewUser(@Valid @RequestBody User user) {
        LOGGER.info(user.toString());
        userService.saveUser(user);
        return user;
    }
}
