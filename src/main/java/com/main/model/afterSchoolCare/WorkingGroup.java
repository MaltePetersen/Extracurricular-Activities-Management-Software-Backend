package com.main.model.afterSchoolCare;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class WorkingGroup extends AfterSchoolCare {

    private boolean isExternal;

    public WorkingGroup() {
        super();
        this.type = Type.WORKING_GROUP;
    }
}
