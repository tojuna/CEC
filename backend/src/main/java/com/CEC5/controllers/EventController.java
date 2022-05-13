package com.CEC5.controllers;

import com.CEC5.emails.EmailService;
import com.CEC5.entity.Event;
import com.CEC5.entity.User;
import com.CEC5.service.EventService;
import com.CEC5.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
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
        e = eventService.findEventById(e.getEvent_id());
        emailService.eventCreated(e);
        return e;
    }

    @GetMapping("/allEvents")
    public List<Event> allEvents() {
        return eventService.allEvents();
    }

    @GetMapping("/{id}")
    public Event eventById(@PathVariable Long id) {
        return eventService.findEventById(id);
    }

    @GetMapping("/filter")
    public List<Event> filteredEvents(@RequestParam(value = "city", required = false) String city,
                                      @RequestParam(value = "status", required = false) String status,
                                      @RequestParam(value = "startTime", required = false)  String startTime,
                                      @RequestParam(value = "endTime", required = false) String endTime,
                                      @RequestParam(value = "keyword", required = false) String keyword,
                                      @RequestParam(value = "organizerName", required = false) String organizerName) {
        if (city != null) LOGGER.info(city);
        if (status != null) LOGGER.info(status);
        if (startTime != null) LOGGER.info(startTime.toString());
        if (endTime != null) LOGGER.info(endTime.toString());
        if (keyword != null) LOGGER.info(keyword);
        if (organizerName != null) LOGGER.info(organizerName);
        LocalDateTime st = null;
        if (startTime != null) st = LocalDateTime.parse(startTime);
        LocalDateTime en = null;
        if (endTime != null) en = LocalDateTime.parse(endTime);
        return eventService.filteredEvents(city, status, st, en, keyword, organizerName);
    }

    @PostMapping("/signUpForEvent")
    public void signUp(@NotEmpty @RequestBody JsonNode requestBody) {
        String email = requestBody.get("email").asText();
        User user = userService.findUser(email);
        Long eventId = requestBody.get("event_id").asLong();
        Event event = eventService.findEventById(eventId);
        if (event.getIsFirstComeFirstServe()) event.getApprovedParticipants().add(user);
        else event.getParticipantsRequiringApproval().add(user);
        eventService.saveEvent(event);
    }

    @GetMapping("/keyword")
    public List<String> keywordSearch(@RequestBody JsonNode jsonNode) {
        return eventService.search(jsonNode.get("keyword").asText());
    }
}
