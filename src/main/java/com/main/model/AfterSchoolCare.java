package com.main.model;

import lombok.Data;

import lombok.Data;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse für die Nachmittagsbetreuung. Pro Schule und Termin (Tag) gibt es
 * ein Objekt für die Nachmittagsbetreuung.
 *
 * @author Bendix Tonn, Markus
 * @since 22.10.2019, 24.10.2019
 * @version 1.0, 1.1
 */

@Entity
@Data
public class AfterSchoolCare {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	@OneToOne
	@JoinColumn(name = "school_id", referencedColumnName = "id")
	private School participatingSchool;

	// TODO: employee to be correctly implemented
//	private User employee;

	@OneToMany(mappedBy = "afterSchoolCare", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Attendance> attendances = new ArrayList<>();

	public void addAttendance(Attendance attendance) {
		this.attendances.add(attendance);
	}

}
