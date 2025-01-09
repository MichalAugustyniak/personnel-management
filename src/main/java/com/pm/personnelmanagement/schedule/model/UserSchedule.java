package com.pm.personnelmanagement.schedule.model;

import com.pm.personnelmanagement.user.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "users_schedules")
public class UserSchedule {
    @EmbeddedId
    private UserScheduleId id = new UserScheduleId();

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @MapsId("scheduleId")
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    public UserScheduleId getId() {
        return id;
    }

    public void setId(UserScheduleId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
