package com.CEC5.service;

import com.CEC5.entity.User;
import com.CEC5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean isEmailPresent(String email) {
        return userRepository.existsById(email);
    }

    public User findUser(String email) {
        return userRepository.findById(email).orElse(null);
    }
}
