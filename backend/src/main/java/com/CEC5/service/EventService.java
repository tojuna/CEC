package com.CEC5.service;

import com.CEC5.SystemDateTime;
import com.CEC5.entity.Event;
import com.CEC5.entity.QEvent;
import com.CEC5.repository.EventRepository;
import com.querydsl.core.BooleanBuilder;
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

    public Event findEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> allEvents() {
        return eventRepository.findAll();
    }

    public List<Event> filteredEvents(String city, String status, LocalDateTime startTime,
                                      LocalDateTime endTime, String keyword, String organizerName) {
        QEvent event = QEvent.event;
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        BooleanBuilder where = new BooleanBuilder();
        if (city != null) where.and(event.address.city.equalsIgnoreCase(city));
        if (status == null || status.equals("active") || status.equals(""))
                where.and((event.signUpDeadline.before(now).and(event.minParticipants
                        .loe(event.approvedParticipants.size()))
                        .and(event.endDateTime.loe(now))).or(event.signUpDeadline.goe(now)));
        if (status != null && status.equals("open")) where.and(event.signUpDeadline.goe(now));
        if (startTime != null) where.and(event.startDateTime.goe(startTime));
        if (endTime != null) where.and(event.endDateTime.loe(endTime));
        if (keyword != null) where.and(event.title.containsIgnoreCase(keyword)
                .or(event.description.containsIgnoreCase(keyword)));
        if (organizerName != null) where.and(event.organizer.screenName.containsIgnoreCase(organizerName));
        return (List<Event>) eventRepository.findAll(where);
    }

    public List<Event> eventsToBeCancelled() {
        QEvent event = QEvent.event;
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        BooleanExpression toCancel = event.signUpDeadline.before(now).and(event.minParticipants
                .lt(event.approvedParticipants.size()))
                .and(event.isCancelledAndEmailSent.eq(Boolean.FALSE));
        return (List<Event>) eventRepository.findAll(toCancel);
    }

    public void cancelGivenEvents(List<Event> events) {
        if (events == null || events.size() == 0) return;
        for (Event event : events) {
            event.setIsCancelledAndEmailSent(true);
        }
        eventRepository.saveAll(events);
    }

    public void setIsCancelledAndEmailSentToFalseGivenEvents() {
        QEvent event = QEvent.event;
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        BooleanExpression toNotCancel = event.signUpDeadline.after(now)
                .and(event.isCancelledAndEmailSent.eq(Boolean.TRUE));
        List<Event> events = (List<Event>) eventRepository.findAll(toNotCancel);
        if (events == null || events.size() == 0) return;
        for (Event e : events) {
            e.setIsCancelledAndEmailSent(false);
        }
        eventRepository.saveAll(events);
    }

    public List<String> search(String keyword) {
        return eventRepository.search(keyword);
    }
}