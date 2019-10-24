package com.main.model;

import com.main.model.userTypes.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse für die Nachmittagsbetreuung. Pro Schule und Termin (Tag) gibt es ein Objekt für die Nachmittagsbetreuung.
 *
 * @author Bendix Tonn
 * @since 22.10.2019
 * @version 1.0
 */

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

    @OneToMany(mappedBy = "afterSchoolCare", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();

    public AfterSchoolCare() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void addAttendance(Attendance attendance) {
        attendances.add(attendance);
    }
}
