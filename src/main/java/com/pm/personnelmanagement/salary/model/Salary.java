package com.pm.personnelmanagement.salary.model;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.UUID;

@Document(collection = "salaries")
public class Salary {
    @Id
    private String id;
    private UUID uuid;
    private String salaryType;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Float dayOvertimeHoursMultiplier;
    private Float nightOvertimeHoursMultiplier;

    public Float getDayOvertimeHoursMultiplier() {
        return dayOvertimeHoursMultiplier;
    }

    public void setDayOvertimeHoursMultiplier(Float dayOvertimeHoursMultiplier) {
        this.dayOvertimeHoursMultiplier = dayOvertimeHoursMultiplier;
    }

    public Float getNightOvertimeHoursMultiplier() {
        return nightOvertimeHoursMultiplier;
    }

    public void setNightOvertimeHoursMultiplier(Float nightOvertimeHoursMultiplier) {
        this.nightOvertimeHoursMultiplier = nightOvertimeHoursMultiplier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getSalaryType() {
        return salaryType;
    }

    public void setSalaryType(String salaryType) {
        this.salaryType = salaryType;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }
}
