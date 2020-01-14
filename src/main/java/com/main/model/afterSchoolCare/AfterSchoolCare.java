package com.main.model.afterSchoolCare;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.*;
import com.main.model.Attendance;
import com.main.model.School;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.Id;

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

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = AfternoonCare.class, name = "1"),
		@JsonSubTypes.Type(value = WorkingGroup.class, name = "2"),
		@JsonSubTypes.Type(value = Remedial.class, name = "3"),
		@JsonSubTypes.Type(value = Amplification.class, name = "4")
})
@Entity
@Data
public abstract class AfterSchoolCare {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	protected Type type;

	public enum Type {
		AFTERNOON_CARE(1), WORKING_GROUP(2), REMEDIAL(3), AMPLIFICATION(4);

		private int id;

		Type(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public Type getById(int id) {
			for (Type type : Type.values()) {
				if (type.getId() == id) {
					return type;
				}
			}

			return null;
		}
	}

	private String name;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	@OneToOne
	@JoinColumn(name = "school_id", referencedColumnName = "id")
	private School participatingSchool;

	// Leiter
	// TODO: employee to be correctly implemented
//	private User employee;

	@OneToMany(mappedBy = "afterSchoolCare", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Attendance> attendances = new ArrayList<>();

	public void addAttendance(Attendance attendance) {
		this.attendances.add(attendance);
	}

}