package com.CEC5.controllers;

import com.CEC5.SystemDateTime;
import com.CEC5.jobs.CancelEvents;
import com.CEC5.service.EventService;
import com.fasterxml.jackson.databind.JsonNode;
import com.mysema.commons.lang.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "Hello World";
    }

    @Autowired
    CancelEvents cancelEvents;

    @Autowired
    EventService eventService;

    @PostMapping("systemTime")
    public void setDateTime(@RequestBody JsonNode jsonNode) {
        Long days = jsonNode.get("days").asLong();
        Long months = jsonNode.get("months").asLong();
        Long year = jsonNode.get("year").asLong();
        SystemDateTime.setCurrentDateTime(days, months, year);
        cancelEvents.cancelEventsJob();
        cancelEvents.setIsCancelledAndEmailSentToFalse();
    }

    @GetMapping("systemTime")
    public LocalDateTime returnSystemDateTime() {
        return SystemDateTime.getCurrentDateTime();
    }

    @GetMapping("systemReport")
    public Map<String, String> systemReport() {
        Pair<Integer, Float> numberOfCreatedEvents =  eventService.numberOfCreatedEvents();
        Pair<Integer, Float> cancelledEvents = eventService.cancelledEvents();
        Pair<Integer, Float> finishedEvents = eventService.finishedEvents();
        Map<String, String> res = new HashMap<>();
        res.put("numberOfCreatedEvents", numberOfCreatedEvents.getFirst().toString());
        res.put("percentageOfPaidEvents", numberOfCreatedEvents.getSecond().toString());
        res.put("numberOfCancelledEvents", cancelledEvents.getFirst().toString());
        res.put("numberOfParticipationRequestsDividedByTheTotalNumberOfMinimumParticipants", cancelledEvents.getSecond().toString());
        res.put("numberOfFinishedEvents", finishedEvents.getFirst().toString());
        res.put("averageNumberOfParticipantsOfTheseEvents", finishedEvents.getSecond().toString());
        return res;
    }

}
