package com.main.model.afterSchoolCare;

import javax.persistence.Entity;

@Entity
public class WorkingGroup extends AfterSchoolCare {
    public WorkingGroup() {
        super();
        this.type = Type.WORKING_GROUP;
    }
}
