package com.CEC5.controllers;

import com.CEC5.emails.EmailService;
import com.CEC5.entity.Event;
import com.CEC5.service.EventService;
import com.CEC5.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    @Autowired
    EventService eventService;

    @Autowired
    EmailService emailService;

    @Autowired
    UserService userService;

    @PostMapping("/createEvent")
    public Event createNewEvent(@Valid @RequestBody Event event) {
        LOGGER.info(event.toString());
        Event e = eventService.saveEvent(event);
        emailService.eventCreated(e);
        return e;
    }

    @GetMapping("/allEvents")
    public List<Event> allEvents() {
        return eventService.allEvents();
    }

    @GetMapping("/filter")
    public List<Event> filteredEvents(@RequestParam(value = "city", required = false) String city,
                                      @RequestParam(value = "status", required = false) String status,
                                      @RequestParam(value = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                      @RequestParam(value = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                      @RequestParam(value = "keyword", required = false) String keyword,
                                      @RequestParam(value = "organizerName", required = false) String organizerName) {
        return eventService.filteredEvents(city, status, startTime, endTime, keyword, organizerName);
    }
}
