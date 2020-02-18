package com.main.model.afterSchoolCare;

public class AfterSchoolCareCreator {

    public static AfterSchoolCare createAfternoonCare() {
        return new AfternoonCare();
    }

    public static AfterSchoolCare createAmplification() {
        return new Amplification();
    }

    public static AfterSchoolCare createWorkingGroup() {
        return new WorkingGroup();
    }

    public static AfterSchoolCare createRemidial() {
        return new Remedial();
    }


}
