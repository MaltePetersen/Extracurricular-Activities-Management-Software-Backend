package com.main.model;

import lombok.Data;

import javax.persistence.*;

import java.time.LocalDateTime;

/**
 * Die Klasse für die Nachmittagsbetreuung. Pro Schule und Termin (Tag) gibt es ein Objekt für die Nachmittagsbetreuung.
 *
 * @author Bendix Tonn
 * @since 22.10.2019
 * @version 1.0
 */

@Entity
@Data
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
   // private User employee;

    // TODO: list of attendances to be implemented

    public AfterSchoolCare() {
    }

}
