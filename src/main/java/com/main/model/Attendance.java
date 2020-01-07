package com.main.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.main.model.afterSchoolCare.AfterSchoolCare;
import lombok.Data;

/***
 * Eine Klasse für die Anwesenheit. Pro Kind und Nachmittagsobjekt gibt es eine Anwesenheit.
 */
@Entity
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "afterSchoolCare_id", referencedColumnName = "id")
    private AfterSchoolCare afterSchoolCare;

//    private User child;

    //Zeitpunkt, an dem das Kind sich beim Betreuer anmeldet
    private LocalDateTime arrivalTime;

    //spätester Zeitpunkt der möglichen Anmeldung, bevor die Eskalation startet
    private LocalDateTime latestArrivalTime;

    //Zeitpunkt, an dem das Kind sich beim Betreuer abmeldet
    private LocalDateTime leaveTime;

    //Zeitpunkt, an dem das Kind durch eine Erlaubnis die etreuung früher veröassen darf
    private LocalDateTime predefinedLeaveTime;

    private String additionalInformation;

}
