package com.CEC5;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoAndEventInfo {
    private String userEmail;
    private String userFullName;
    private String organizerSceeenName;
    private String eventTitle;
    private Long eventId;
}
