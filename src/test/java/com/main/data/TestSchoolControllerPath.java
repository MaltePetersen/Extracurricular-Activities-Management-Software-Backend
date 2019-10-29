package com.main.data;

public enum TestSchoolControllerPath {
	SCHOOLS("/api/schools");

	private String path;

	public String getUri() {
		return path;
	}

	private TestSchoolControllerPath(String path) {
		this.path = path;
	}
}
