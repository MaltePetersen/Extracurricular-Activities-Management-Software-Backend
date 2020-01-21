package com.main.model.afterSchoolCare;

import org.aspectj.lang.annotation.After;

public class AfterSchoolCareCreator {

    public static AfterSchoolCare createAfternoonCare(){
        return new AfternoonCare();
    }

    public static AfterSchoolCare createAmplification(){
        return new Amplification();
    }

    public static AfterSchoolCare createWorkingGroup(){
        return new WorkingGroup();
    }

}
