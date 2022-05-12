package com.CEC5.controllers;

import com.CEC5.SystemDateTime;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }

    @PostMapping("systemTime")
    public void setDateTime(@RequestBody JsonNode jsonNode) {
        Long days = jsonNode.get("days").asLong();
        Long months = jsonNode.get("months").asLong();
        Long year = jsonNode.get("year").asLong();
        SystemDateTime.setCurrentDateTime(days, months, year);
    }

    @GetMapping("systemTime")
    public LocalDateTime returnSystemDateTime() {
        return SystemDateTime.getCurrentDateTime();
    }
}
