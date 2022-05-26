package com.CEC5.controllers;

import com.CEC5.SystemDateTime;
import com.CEC5.emails.EmailService;
import com.CEC5.entity.*;
import com.CEC5.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    ForumMessageService forumMessageService;

    @Autowired
    SignUpEventService signUpEventService;

    @Autowired
    RejectOrApprovalForEventService rejectOrApprovalForEventService;

    @PostMapping("/createEvent")
    public Event createNewEvent(@Valid @RequestBody Event event) {
        LOGGER.info(event.toString());
        event.setCreationDateTime(SystemDateTime.getCurrentDateTime());
        Event e = eventService.saveEvent(event);
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
    public Event signUp(@NotEmpty @RequestBody JsonNode requestBody) {
        String email = requestBody.get("email").asText();
        User user = userService.findUser(email);
        Long eventId = requestBody.get("event_id").asLong();
        Event event = eventService.findEventById(eventId);
        if (event.getMaxParticipants() < event.getApprovedParticipants().size())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Max participants reached");
        if (event.getIsFirstComeFirstServe()) event.getApprovedParticipants().add(user);
        else event.getParticipantsRequiringApproval().add(user);
        SignUpEvent signUpEvent = new SignUpEvent(user, event, SystemDateTime.getCurrentDateTime());
        signUpEventService.save(signUpEvent);
        emailService.userHasSignedUpForEvent(event, user);
        return eventService.saveEvent(event);
    }

    @PostMapping("/keyword")
    public List<String> keywordSearch(@RequestBody JsonNode jsonNode) {
        return eventService.search(jsonNode.get("keyword").asText());
    }

    @PostMapping("/approveUserForEvent")
    public User approveUserForEvent(@NotEmpty @RequestBody JsonNode requestBody) {
        String userToBeApprovedEmail = requestBody.get("userToBeApprovedEmail").asText();
        String userWhoIsTryingToApproveEmail = requestBody.get("userWhoIsTryingToApproveEmail").asText();
        Long eventId = requestBody.get("event_id").asLong();
        Event event = eventService.findEventById(eventId);
        User toBeApproved = userService.findUser(userToBeApprovedEmail);
        if (toBeApproved.getOrganization() && !event.getOrganizer().getEmail().equals(userWhoIsTryingToApproveEmail))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Organization can't register for event or wrong user");
        if (event.getApprovedParticipants().size() < event.getMaxParticipants()
                && event.getParticipantsRequiringApproval().contains(toBeApproved)) {
            event.getApprovedParticipants().add(toBeApproved);
            event.getParticipantsRequiringApproval().remove(toBeApproved);
        }
        else throw new ResponseStatusException(HttpStatus.CONFLICT, "Something went wrong");
        eventService.saveEvent(event);
        RejectOrApprovalForEvent rejectOrApprovalForEvent = new RejectOrApprovalForEvent(false,
                SystemDateTime.getCurrentDateTime(),
                toBeApproved,
                event);
        rejectOrApprovalForEventService.save(rejectOrApprovalForEvent);
        emailService.participationSignUpRequestApproved(event, toBeApproved);
        return userService.findUser(userWhoIsTryingToApproveEmail);
    }

    @PostMapping("/rejectUserForEvent")
    public User rejectUser(@NotEmpty @RequestBody JsonNode requestBody) {
        String userToBeRejectedEmail = requestBody.get("userToBeRejectedEmail").asText();
        String userWhoIsTryingToRejectEmail = requestBody.get("userWhoIsTryingToRejectEmail").asText();
        Long eventId = requestBody.get("event_id").asLong();
        Event event = eventService.findEventById(eventId);
        User toBeRejected = userService.findUser(userToBeRejectedEmail);
        if (!event.getOrganizer().getEmail().equals(userWhoIsTryingToRejectEmail))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Given user can't reject");
        if (event.getParticipantsRequiringApproval().contains(toBeRejected))
            event.getParticipantsRequiringApproval().remove(toBeRejected);
        else throw new ResponseStatusException(HttpStatus.CONFLICT, "Something went wrong");
        eventService.saveEvent(event);
        emailService.participationSignUpRequestRejected(event, toBeRejected);
        return userService.findUser(userWhoIsTryingToRejectEmail);
    }

    @PostMapping("/participantForumMessage")
    public Event participantForumMessage(@Valid @RequestBody ForumMessage forumMessage) {
        Event event = forumMessage.getEvent();
        User creator = userService.findUser(forumMessage.getMessageCreator().getEmail());
        event = eventService.findEventById(event.getEvent_id());
        if (event.getSignUpDeadline().compareTo(SystemDateTime.getCurrentDateTime()) > 0 ||
        event.getEndDateTime().plusDays(7).compareTo(SystemDateTime.getCurrentDateTime()) < 0)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sign up deadline has not passed or event end + 7 days has passed");
//        if (!event.getApprovedParticipants().contains(creator))
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Creator not in approved participants");
        forumMessageService.save(forumMessage);
        event.getParticipantForumMessageList().add(forumMessage);
        eventService.saveEvent(event);
        forumMessage.setEvent(event);
        emailService.newMessageInForum(forumMessage);
        return eventService.findEventById(event.getEvent_id());
    }

    @PostMapping("/signUpForumMessage")
    public Event signUpForumMessage(@Valid @RequestBody ForumMessage forumMessage) {
        Event event = forumMessage.getEvent();
        event = eventService.findEventById(event.getEvent_id());
        if (event.getSignUpDeadline().compareTo(SystemDateTime.getCurrentDateTime()) < 0)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sign up deadline has passed");
        forumMessageService.save(forumMessage);
        event.getSignUpMessageList().add(forumMessage);
        eventService.saveEvent(event);
        forumMessage.setEvent(event);
        emailService.newMessageInForum(forumMessage);
        return eventService.findEventById(event.getEvent_id());
    }

    @PostMapping("/closeParticipantForum")
    public Event closeParticipantForum(@Valid @RequestBody JsonNode jsonNode) {
        String email = jsonNode.get("email").asText();
        Long event_id = jsonNode.get("event_id").asLong();
        Event event = eventService.findEventById(event_id);
        if (!event.getOrganizer().getEmail().equals(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only organizer can close");
        event.setParticipantForumClosedByOrganizer(true);
        return eventService.saveEvent(event);
    }
}
