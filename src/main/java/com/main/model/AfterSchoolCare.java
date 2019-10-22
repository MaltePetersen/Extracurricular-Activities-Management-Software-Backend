package com.main.model;

import com.main.model.userTypes.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AfterSchoolCare {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @OneToOne
    @JoinColumn(name = "school_id", referencedColumnName = "id")
    private School participatingSchool;

    // TODO: employee to be correctly implemented
    private User employee;

    public AfterSchoolCare() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public School getParticipatingSchool() {
        return participatingSchool;
    }

    public void setParticipatingSchool(School participatingSchool) {
        this.participatingSchool = participatingSchool;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
}
