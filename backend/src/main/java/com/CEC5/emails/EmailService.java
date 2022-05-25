package com.CEC5.emails;

import com.CEC5.UserInfoAndEventInfo;
import com.CEC5.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    void sendEmailAsynchronously(SimpleMailMessage message) {
        javaMailSender.send(message);
    }

    public void eventCancelledEmail(List<UserInfoAndEventInfo> userInfoAndEventInfoList) {
        for (UserInfoAndEventInfo userInfoAndEventInfo: userInfoAndEventInfoList) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userInfoAndEventInfo.getUserEmail());
            message.setSubject(userInfoAndEventInfo.getEventTitle() + " Cancelled");
            message.setText("Hello " + userInfoAndEventInfo.getUserFullName() + "," +
                    " We regret to inform you that the event " + userInfoAndEventInfo.getEventTitle() +
                    " with Event Id " + userInfoAndEventInfo.getEventId() +
                    " which was being organized by " + userInfoAndEventInfo.getOrganizerSceeenName() +
                    " has been cancelled due to less number of participants");
            sendEmailAsynchronously(message);
        }
    }

    public void newUserCreated(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome to CEC " + user.getScreenName());
        message.setText("Hello " + user.getFullName() + ", new account has been created for the email "
                + user.getEmail());
        sendEmailAsynchronously(message);
    }

    public void eventCreated(Event event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getOrganizer().getEmail());
        message.setSubject("Event Created: " + event.getTitle());
        message.setText("Hello, a new event with event id: " + event.getEvent_id() +
                " and title " + event.getTitle() + " has been created");
        sendEmailAsynchronously(message);
    }

    public void participationSignUpRequestApproved(Event event, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Sign Up request approved for event: " + event.getTitle());
        message.setText("Hello " + user.getFullName() +
                ", your sign up request for the event " + event.getTitle() +
                " and event ID " + event.getEvent_id() + "has been approved");
        sendEmailAsynchronously(message);
    }

    public void participationSignUpRequestRejected(Event event, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Sign Up request rejected for event: " + event.getTitle());
        message.setText("Hello " + user.getFullName() +
                ", your sign up request for the event " + event.getTitle() +
                " and event ID " + event.getEvent_id() + "has been rejected");
        sendEmailAsynchronously(message);
    }

    public void receivedReview(Reviews reviews) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(reviews.getReviewedUser().getEmail());
        message.setSubject("You have received a new review from " + reviews.getReviewedBy().getScreenName());
        message.setText("Hello " + reviews.getReviewedUser().getFullName() +
                ", you have received a new review from " + reviews.getReviewedBy().getScreenName() + "." +
                " Review: " + reviews.getDescription() + " Rating: " + reviews.getRating());
        sendEmailAsynchronously(message);
    }

    public void newMessageInForum(ForumMessage forumMessage) {
        forumMessage(forumMessage.getEvent(), forumMessage.getMessage());
    }

    private void forumMessage(Event event, String message) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo(event.getOrganizer().getEmail());
        m.setSubject("New message posted on event: " + event.getTitle());
        m.setText("Hello " + event.getOrganizer().getFullName() +
                ", a new message has been posted on the event titled: " + event.getTitle() +
                " and Event No.: " + event.getEvent_id() +
                " Message: " + message);
        sendEmailAsynchronously(m);
    }

    public void userHasSignedUpForEvent(Event event, User u) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo(u.getEmail());
        m.setSubject("Sign up request received");
        m.setText("Hello " + u.getFullName() +
                ", your sign up request has been received for the event id " + event.getTitle());
        sendEmailAsynchronously(m);
    }

}
