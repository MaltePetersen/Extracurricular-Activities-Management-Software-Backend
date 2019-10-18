package com.main.model.userTypes.interfaces;

import java.util.List;

import com.main.model.School;

public interface ISchoolCoordinator extends IUser{
	boolean isSchoolCoordinator();
	void setSchoolCoordinator(boolean isSchoolCoordinator);
	
	List<School> getSchoolCoordinatorsSchools();
	void setSchoolCoordinatorsSchools(List<School> schoolCoordinatorsSchools);
}
