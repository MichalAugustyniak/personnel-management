package com.pm.personnelmanagement.schedule.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserScheduleId implements Serializable {
    private Long userId;
    private Long scheduleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
}
