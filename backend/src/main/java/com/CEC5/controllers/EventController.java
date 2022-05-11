package com.CEC5.controllers;

import com.CEC5.emails.EmailService;
import com.CEC5.entity.Event;
import com.CEC5.service.EventService;
import com.CEC5.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

//    @GetMapping("/filter")
//    public List<Event> filteredEvents(@RequestBody FilteredEventsStructure filteredEventsStructure) {
//        return eventService.filteredEvents(filteredEventsStructure.getCity(),
//                filteredEventsStructure.getStatus(),
//                filteredEventsStructure.getStartTime(),
//                filteredEventsStructure.getEndTime(),
//                filteredEventsStructure.getKeyword(),
//                filteredEventsStructure.getOrganizerName());
//    }
}
