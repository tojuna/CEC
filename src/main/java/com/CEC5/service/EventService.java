package com.CEC5.service;

import com.CEC5.SystemDateTime;
import com.CEC5.entity.Event;
import com.CEC5.entity.QEvent;
import com.CEC5.repository.EventRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public List<Event> allEvents() {
        return eventRepository.findAll();
    }

    public List<Event> filteredEvents(String city, String status, LocalDateTime startTime, LocalDateTime endTime, String keyword, String organizerName) {
        if (status != null && status.equals("all")) return allEvents();
        QEvent event = QEvent.event;
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        BooleanExpression cityCheck = (city == null) ? Expressions.TRUE: event.address.city.eq(city);
        BooleanExpression activeCheck = (status == null || status.equals("active") || status.equals("")) ?
                (event.signUpDeadline.before(now).and(event.minParticipants
                        .gt(event.approvedParticipants.size().add(-1)))
                        .and(event.endDateTime.lt(now))).or(event.signUpDeadline.after(now))
                : Expressions.TRUE;
        BooleanExpression openForRegistration = (!status.equals("open")) ? Expressions.TRUE:
                event.signUpDeadline.after(now);
        BooleanExpression start = (startTime == null) ? event.startDateTime.after(now):
                event.startDateTime.after(startTime);
        BooleanExpression end = (endTime == null) ? Expressions.TRUE: event.endDateTime.before(endTime);
        BooleanExpression titleMatch = (keyword == null) ? Expressions.TRUE: event.title.containsIgnoreCase(keyword);
        BooleanExpression descMatch = (keyword == null) ? Expressions.TRUE: event.description.containsIgnoreCase(keyword);
        BooleanExpression organizerMatch = (organizerName == null) ? Expressions.TRUE:
                event.organizer.screenName.containsIgnoreCase(organizerName);
        return (List<Event>) eventRepository.findAll(cityCheck.and(activeCheck)
                .and(openForRegistration).and(start).and(end)
                .and(titleMatch).and(descMatch).and(organizerMatch));
    }
}