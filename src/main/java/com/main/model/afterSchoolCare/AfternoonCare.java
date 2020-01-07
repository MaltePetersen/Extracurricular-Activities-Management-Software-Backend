package com.main.model.afterSchoolCare;

import javax.persistence.Entity;

@Entity
public class AfternoonCare extends AfterSchoolCare {
    public AfternoonCare() {
        super();
        this.type = Type.AFTERNOON_CARE;
    }
}
