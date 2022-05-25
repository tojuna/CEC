package com.CEC5.service;

import com.CEC5.SystemDateTime;
import com.CEC5.entity.*;
import com.CEC5.repository.EventRepository;
import com.CEC5.repository.SignUpEventRepository;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    SignUpEventRepository signUpEventRepository;

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
                .gt(event.approvedParticipants.size()))
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

    public Pair<Integer, Float> numberOfCreatedEvents() {
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        QEvent event = QEvent.event;
        BooleanExpression time = event.creationDateTime.goe(now.minusDays(90));
        List<Event> events = (List<Event>) eventRepository.findAll(time);
        if (events == null || events.size() == 0) return new Pair<>(0, 0.0F);
        int countOfPaid = 0;
        for (Event e: events) {
            if (e.getFee() > 0) countOfPaid++;
        }
        return new Pair<>(events.size(), (float)countOfPaid * 100 / events.size());
    }

    public Pair<Integer, Float> cancelledEvents() {
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        QEvent event = QEvent.event;
        BooleanExpression condition = event.signUpDeadline.goe(now.minusDays(90))
                .and(event.isCancelledAndEmailSent.eq(Boolean.TRUE));
        List<Event> events = (List<Event>) eventRepository.findAll(condition);
        int totalParticipationRequests = 0;
        int totalMinimumParticipants = 0;
        for (Event e: events) {
            QSignUpEvent sign = QSignUpEvent.signUpEvent;
            BooleanExpression c = sign.event.event_id.eq(e.getEvent_id());
            Long count = signUpEventRepository.count(c);
            totalParticipationRequests += count;
            totalMinimumParticipants += e.getMinParticipants();
        }
        return new Pair<>(events.size(), (float)totalParticipationRequests * 100 / totalMinimumParticipants);
    }

    public Pair<Integer, Float> finishedEvents() {
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        QEvent event = QEvent.event;
        BooleanExpression condition = event.endDateTime.loe(now).and(event.endDateTime.goe(now.minusDays(90)))
                .and(event.isCancelledAndEmailSent.eq(Boolean.FALSE));
        List<Event> events = (List<Event>) eventRepository.findAll(condition);
        if (events.size() == 0) new Pair<>(0, 0.0F);
        int tot = 0;
        for (Event e: events) {
            tot += e.getApprovedParticipants().size();
        }
        return new Pair<>(tot, (float)tot * 100 / events.size());
    }

    public Long numberOfFinishedEventsWhereUserHasParticipated(User u) {
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        QEvent event = QEvent.event;
        BooleanExpression condition = event.endDateTime.loe(now).and(event.endDateTime.goe(now.minusDays(90)))
                .and(event.isCancelledAndEmailSent.eq(Boolean.FALSE)).and(event.approvedParticipants.contains(u));
        return eventRepository.count(condition);
    }

    public Pair<Integer, Float> numberOfCreatedEventsByUser(User u) {
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        QEvent event = QEvent.event;
        BooleanExpression condition = event.creationDateTime.goe(now.minusDays(90))
                .and(event.organizer.email.eq(u.getEmail()));
        List<Event> events = (List<Event>) eventRepository.findAll(condition);
        if (events == null || events.size() == 0) return new Pair<>(0, 0.0F);
        int countOfPaid = 0;
        for (Event e: events) {
            if (e.getFee() > 0) countOfPaid++;
        }
        return new Pair<>(events.size(), (float)countOfPaid * 100 / events.size());
    }

    public Pair<Integer, Float> numberOfCancelledEventsByUser(User u) {
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        QEvent event = QEvent.event;
        BooleanExpression condition = event.signUpDeadline.goe(now.minusDays(90))
                .and(event.organizer.email.eq(u.getEmail())).and(event.isCancelledAndEmailSent.eq(Boolean.TRUE));
        List<Event> events = (List<Event>) eventRepository.findAll(condition);
        if (events == null || events.size() == 0) return new Pair<>(0, 0.0F);
        int totalParticipationRequests = 0;
        int totalMinimumParticipants = 0;
        for (Event e: events) {
            QSignUpEvent sign = QSignUpEvent.signUpEvent;
            BooleanExpression c = sign.event.event_id.eq(e.getEvent_id());
            Long count = signUpEventRepository.count(c);
            totalParticipationRequests += count;
            totalMinimumParticipants += e.getMinParticipants();
        }
        return new Pair<>(events.size(), (float)totalParticipationRequests * 100 / totalMinimumParticipants);
    }

    public Pair<Integer, Float> finishedEvents(User u) {
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        QEvent event = QEvent.event;
        BooleanExpression condition = event.endDateTime.loe(now).and(event.endDateTime.goe(now.minusDays(90)))
                .and(event.isCancelledAndEmailSent.eq(Boolean.FALSE)).and(event.organizer.email.eq(u.getEmail()));
        List<Event> events = (List<Event>) eventRepository.findAll(condition);
        if (events == null || events.size() == 0) return new Pair<>(0, 0.0F);
        if (events.size() == 0) new Pair<>(0, 0.0F);
        int tot = 0;
        for (Event e: events) {
            tot += e.getApprovedParticipants().size();
        }
        return new Pair<>(tot, (float)tot * 100 / events.size());
    }

    public Pair<Integer, Integer> numberOfPaidEventsFinished(User u) {
        LocalDateTime now = SystemDateTime.getCurrentDateTime();
        QEvent event = QEvent.event;
        BooleanExpression condition = event.endDateTime.loe(now).and(event.endDateTime.goe(now.minusDays(90)))
                .and(event.isCancelledAndEmailSent.eq(Boolean.FALSE)).and(event.organizer.email.eq(u.getEmail()))
                .and(event.fee.gt(0));
        List<Event> events = (List<Event>) eventRepository.findAll(condition);
        int revenue = 0;
        for (Event e: events) {
            revenue += e.getFee() * e.getApprovedParticipants().size();
        }
        return new Pair<>(events.size(), revenue);
    }

}