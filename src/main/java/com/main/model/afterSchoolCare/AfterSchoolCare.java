package com.main.model.afterSchoolCare;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.main.model.Attendance;
import com.main.model.School;
import com.main.model.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Die Klasse für die Nachmittagsbetreuung. Pro Schule und Termin (Tag) gibt es
 * ein Objekt für die Nachmittagsbetreuung.
 *
 * @author Bendix Tonn, Markus
 * @version 1.0, 1.1
 * @since 22.10.2019, 24.10.2019
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

        public static Type getById(int id) {
            for (Type type : Type.values()) {
                if (type.getId() == id) {
                    return type;
                }
            }

            return null;
        }
    }

    /**
     * closed gibt an, dass das AfterSchoolCare abgeschlossen ist und nicht mehr bearbeitet werden kann
     */
    private boolean closed;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", referencedColumnName = "id")
    private School participatingSchool;

    // Leiter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

    @OneToMany(mappedBy = "afterSchoolCare", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();

    public void addAttendance(Attendance attendance) {
        if(!attendances.contains(attendance))
        {
            this.attendances.add(attendance);
        }
    }

    public static Map<Integer, String> getTypes() {
        Map<Integer, String> types = new HashMap<>();

        types.put(Type.AFTERNOON_CARE.getId(), "Nachmittagsbetreuung");
        types.put(Type.WORKING_GROUP.getId(), "AG");
        types.put(Type.REMEDIAL.getId(), "Nachhilfe");
        types.put(Type.AMPLIFICATION.getId(), "Verstärkung");

        return types;
    }
}
