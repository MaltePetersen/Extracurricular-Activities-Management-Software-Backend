package com.main.model.afterSchoolCare;

import javax.persistence.Entity;

@Entity
public class Amplification extends AfterSchoolCare {
    public Amplification() {
        super();
        this.type = Type.AMPLIFICATION;
    }
}
