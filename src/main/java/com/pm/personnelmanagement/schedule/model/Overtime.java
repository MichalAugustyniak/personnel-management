package com.pm.personnelmanagement.schedule.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
public class Overtime extends ScheduleDay {

    private Boolean isObligatory;

    public Boolean getObligatory() {
        return isObligatory;
    }

    public void setObligatory(Boolean obligatory) {
        isObligatory = obligatory;
    }
}
