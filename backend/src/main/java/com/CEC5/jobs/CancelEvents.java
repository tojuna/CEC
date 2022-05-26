package com.CEC5.jobs;

import com.CEC5.UserInfoAndEventInfo;
import com.CEC5.emails.EmailService;
import com.CEC5.entity.Event;
import com.CEC5.entity.User;
import com.CEC5.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class CancelEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger(CancelEvents.class);

    @Autowired
    EventService eventService;

    @Autowired
    EmailService emailService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void cancelEventsJob() {
        LOGGER.info("Hello");
        List<Event> eventsList = eventService.eventsToBeCancelled();
        if (eventsList != null && eventsList.size() > 0) {
            List<UserInfoAndEventInfo> userInfoAndEventInfoList = new ArrayList<>();
            for (Event event: eventsList) {
                Set<User> participants = event.getApprovedParticipants();
                if (participants == null || participants.size() == 0) continue;
                for (User user: participants) {
                    userInfoAndEventInfoList.add(new UserInfoAndEventInfo(user.getEmail(),
                            user.getFullName(),
                            event.getOrganizer().getScreenName(),
                            event.getTitle(),
                            event.getEvent_id()));
                }
                participants = event.getParticipantsRequiringApproval();
                if (participants == null || participants.size() == 0) continue;
                for (User user: participants) {
                    userInfoAndEventInfoList.add(new UserInfoAndEventInfo(user.getEmail(),
                            user.getFullName(),
                            event.getOrganizer().getScreenName(),
                            event.getTitle(),
                            event.getEvent_id()));
                }
            }
            emailService.eventCancelledEmail(userInfoAndEventInfoList);
        }
        eventService.cancelGivenEvents(eventsList);
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void setIsCancelledAndEmailSentToFalse() {
        eventService.setIsCancelledAndEmailSentToFalseGivenEvents();
    }
}
