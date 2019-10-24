package com.main.model;

import com.main.model.userTypes.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/***
 * Eine Klasse für die Anwesenheit. Pro Kind und Nachmittagsobjekt gibt es eine Anwesenheit.
 */
@Entity
public class Attendance {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "afterSchoolCare_id", referencedColumnName = "id")
    private AfterSchoolCare afterSchoolCare;

    private User child;

    //Zeitpunkt, an dem das Kind sich beim Betreuer anmeldet
    private LocalDateTime arrivalTime;

    //spätester Zeitpunkt der möglichen Anmeldung, bevor die Eskalation startet
    private LocalDateTime latestArrivalTime;

    //Zeitpunkt, an dem das Kind sich beim Betreuer abmeldet
    private LocalDateTime leaveTime;

    //Zeitpunkt, an dem das Kind durch eine Erlaubnis die etreuung früher veröassen darf
    private LocalDateTime predefinedLeaveTime;

    private String additionalInformation;

    public Attendance(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AfterSchoolCare getAfterSchoolCare() {
        return afterSchoolCare;
    }

    public void setAfterSchoolCare(AfterSchoolCare afterSchoolCare) {
        this.afterSchoolCare = afterSchoolCare;
    }

    public User getChild() {
        return child;
    }

    public void setChild(User child) {
        this.child = child;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getLatestArrivalTime() {
        return latestArrivalTime;
    }

    public void setLatestArrivalTime(LocalDateTime latestArrivalTime) {
        this.latestArrivalTime = latestArrivalTime;
    }

    public LocalDateTime getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(LocalDateTime leaveTime) {
        this.leaveTime = leaveTime;
    }

    public LocalDateTime getPredefinedLeaveTime() {
        return predefinedLeaveTime;
    }

    public void setPredefinedLeaveTime(LocalDateTime predefinedLeaveTime) {
        this.predefinedLeaveTime = predefinedLeaveTime;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
