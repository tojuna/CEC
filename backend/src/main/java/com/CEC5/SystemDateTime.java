package com.CEC5;

import java.time.LocalDateTime;

public class SystemDateTime {

    private static long addedYears = 0;
    private static long addedDays = 0;
    private static long addedMonths = 0;
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now().plusDays(addedDays).plusMonths(addedMonths).plusYears(addedYears);
    }

    public static void setCurrentDateTime(Long days, Long months, Long years) {
        addedDays = (days == null) ? 0: days;
        addedMonths = (months == null) ? 0: months;
        addedYears = (years == null) ? 0: years;
    }
}
