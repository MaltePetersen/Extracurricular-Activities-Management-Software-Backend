package com.main.data;

public enum TestAfterSchoolCareControllerPath {
	AFTER_SCHOOL_CARES("/api/after_school_cares");

	private String path;

	public String getUri() {
		return path;
	}

	private TestAfterSchoolCareControllerPath(String path) {
		this.path = path;
	}
}
