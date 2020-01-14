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
import com.main.model.interfaces.IChild;
import com.main.model.interfaces.IUser;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User child;

    //Zeitpunkt, an dem das Kind sich beim Betreuer anmeldet
    private LocalDateTime arrivalTime;

    //spätester Zeitpunkt der möglichen Anmeldung, bevor die Eskalation startet
    //private LocalDateTime latestArrivalTime;

    //Zeitpunkt, an dem das Kind sich beim Betreuer abmeldet
    private LocalDateTime leaveTime;

    //Zeitpunkt, an dem das Kind durch eine Erlaubnis die Betreuung früher verlassen darf
    //private LocalDateTime predefinedLeaveTime;

    private String note;

    public int determineStatus() {

        if (arrivalTime == null && leaveTime == null) {
            if (afterSchoolCare.getStartTime().isBefore(LocalDateTime.now())) {
                return AttendanceStatus.REGISTERED;
            } else return AttendanceStatus.OVERDUE;
        } else if (leaveTime == null) return AttendanceStatus.PRESENT;
        else if (arrivalTime != null) return AttendanceStatus.GONE;
        else return AttendanceStatus.ERROR;

    }


    // Freitextsuche nach Vor- und Nachname; Sortierung nach Klassen und Anwesenheitsliste nach Datum
    //Array Schülerdaten zur Darstellung der anwesenden Kinder (Name, Klasse, Betreuungsende und Info)

    public static class AttendanceStatus {
        static final int ERROR = 0;

        static final int REGISTERED = 1;

        static final int OVERDUE = 2;

        static final int PRESENT = 3;

        static final int GONE = 4;

    }

}

