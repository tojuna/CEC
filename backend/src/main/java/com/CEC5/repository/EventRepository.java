package com.CEC5.repository;

import com.CEC5.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    @Query(value = "SELECT title FROM event where title like %:keyword% limit 8", nativeQuery = true)
    public List<String> search(@Param("keyword") String keyword);
}
