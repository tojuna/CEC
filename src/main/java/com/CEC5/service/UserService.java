package com.CEC5.service;

import com.CEC5.entity.User;
import com.CEC5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
