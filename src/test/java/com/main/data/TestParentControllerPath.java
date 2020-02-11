package com.main.data;

public enum TestParentControllerPath {
	ADDCHILD("/addChild"), SCHOOLS("/api/parent/schools"), AFTER_SCHOOL_CARES("/api/parent/after_school_cares"), BOOKED_AFTER_SCHOOL_CARES("/api/parent/booked_after_school_cares"), CREATE_CHILD("/api/parent/child"),
	CHILDREN("/api/parent/children"), UPDATE_CHILD("/api/parent//child/"), GET_TYPES("/api/parent/after_school_cares/types");
	private String path;
	
	public String getUri() {
		return path;
	}
	
	private TestParentControllerPath(String path) {
		this.path = path;
	}
}
