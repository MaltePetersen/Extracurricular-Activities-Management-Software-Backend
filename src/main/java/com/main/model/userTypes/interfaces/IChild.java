package com.main.model.userTypes.interfaces;

import com.main.model.School;

public interface IChild extends IUser {
	String getSchoolClass();
	void setSchoolClass(String schoolClass);
	
	School getChildSchool();
	void setChildSchool(School school);
}
