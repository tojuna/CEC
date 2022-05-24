package com.CEC5.controllers;

import com.CEC5.emails.EmailService;
import com.CEC5.entity.Event;
import com.CEC5.entity.Reviews;
import com.CEC5.entity.User;
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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    EventService eventService;

    @Autowired
    EmailService emailService;

    @Autowired
    ReviewsService reviewsService;

    @Autowired
    SignUpEventService signUpEventService;

    @Autowired
    RejectOrApprovalForEventService rejectOrApprovalForEventService;

    @GetMapping
    public User getUser(@NotEmpty @RequestBody String email) {
        LOGGER.info(email);
        return userService.findUser(email);
    }

    @PostMapping("/login")
    public User login(@NotEmpty @RequestBody JsonNode requestBody) {
        User u = userService.findUser(requestBody.get("email").asText());
        if (u == null || !u.getPassword().equals(requestBody.get("password").asText()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong email or password");
        return u;
    }

    @PostMapping("/newUser")
    public User createNewUser(@Valid @RequestBody User user) {
        if (userService.isEmailPresent(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        User u = userService.saveUser(user);
        emailService.newUserCreated(u);
        return u;
    }

    @PostMapping("/review")
    public User reviewUser(@Valid @RequestBody Reviews reviews) {
        User reviewedUser = userService.findUser(reviews.getReviewedUser().getEmail());
        User reviewedBy = userService.findUser(reviews.getReviewedBy().getEmail());
        Event event = eventService.findEventById(reviews.getEvent().getEvent_id());
        if (!event.getOrganizer().getEmail().equals(reviewedBy.getEmail())
                && !event.getOrganizer().getEmail().equals(reviewedUser.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "One user has to be event organizer");
        reviewsService.save(reviews);
        int n = reviewedUser.getReviewsReceivedAsOrganizerList().size();
        if (event.getOrganizer().getEmail().equals(reviewedUser.getEmail())) {
            reviewedUser.getReviewsReceivedAsOrganizerList().add(reviews);
            float avg = reviewedUser.getReviewsReceivedAsOrganizerAverage();
            reviewedUser.setReviewsReceivedAsOrganizerAverage(((avg * n) + reviews.getRating()) / (n + 1));
        }
        else if (event.getOrganizer().getEmail().equals(reviewedBy.getEmail())) {
            reviewedUser.getReviewsReceivedAsParticipantList().add(reviews);
            float avg = reviewedUser.getReviewsReceivedAsParticipantAverage();
            reviewedUser.setReviewsReceivedAsParticipantAverage(((avg * n) + reviews.getRating()) / (n + 1));
        }
        reviews.setReviewedUser(reviewedUser);
        reviews.setReviewedBy(reviewedBy);
        reviews.setEvent(event);
        userService.saveUser(reviewedUser);
        emailService.receivedReview(reviews);
        return reviewedUser;
    }

    @GetMapping("/userReport")
    public Map<String, String> userReport(@RequestBody JsonNode jsonNode) {
        String email = jsonNode.get("email").asText();
        User u = userService.findUser(email);
        Map<String, String> res = new HashMap<>();
        res.put("numberOfSignedUpEvents", signUpEventService.numberOfSignedUpEvents(email).toString());
        res.put("numberOfRejectsOrApprovals", rejectOrApprovalForEventService.numberOfRejectsOrApprovals(email).toString());
        res.put("numberOfFinishedEventsWhereUserHasParticipated", eventService.numberOfFinishedEventsWhereUserHasParticipated(u).toString());
        return res;
    }

//    @GetMapping("/organizerReport")
//    public Map<String, String> organizerReport(@RequestBody JsonNode jsonNode) {
//        String email = jsonNode.get("email").asText();
//
//    }
}
