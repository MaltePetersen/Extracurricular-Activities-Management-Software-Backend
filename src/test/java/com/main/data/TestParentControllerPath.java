package com.main.data;

public enum TestParentControllerPath {
	ADDCHILD("/addChild"), SCHOOLS("/api/parent/schools"), AFTER_SCHOOL_CARES("/api/parent/after_school_cares");
	private String path;
	
	public String getUri() {
		return path;
	}
	
	private TestParentControllerPath(String path) {
		this.path = path;
	}
}
