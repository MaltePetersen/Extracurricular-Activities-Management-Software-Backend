package com.main.model.afterSchoolCare;

import javax.persistence.Entity;

@Entity
public class Remedial extends AfterSchoolCare {
    public Remedial() {
        super();
        this.type = Type.REMEDIAL;
    }
}
