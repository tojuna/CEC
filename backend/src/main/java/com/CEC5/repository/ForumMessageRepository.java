package com.CEC5.repository;

import com.CEC5.entity.ForumMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumMessageRepository extends JpaRepository<ForumMessage, Long> {
}
