package com.pm.personnelmanagement.schedule.model;

import com.pm.personnelmanagement.user.model.User;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "substitutions")
public class Substitution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User substitutingUser;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User substitutedUser;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ScheduleDay scheduleDay;
    private String reason;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false, unique = true)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID uuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSubstitutingUser() {
        return substitutingUser;
    }

    public void setSubstitutingUser(User substitutingUser) {
        this.substitutingUser = substitutingUser;
    }

    public User getSubstitutedUser() {
        return substitutedUser;
    }

    public void setSubstitutedUser(User substitutedUser) {
        this.substitutedUser = substitutedUser;
    }

    public ScheduleDay getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(ScheduleDay scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
