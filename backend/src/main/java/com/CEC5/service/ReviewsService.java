package com.CEC5.service;

import com.CEC5.entity.Reviews;
import com.CEC5.repository.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewsService {

    @Autowired
    ReviewsRepository repository;

    public Reviews save(Reviews reviews) {
        return repository.save(reviews);
    }
}
