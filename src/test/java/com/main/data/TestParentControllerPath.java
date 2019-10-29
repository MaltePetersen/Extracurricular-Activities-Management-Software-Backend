package com.main.data;

public enum TestParentControllerPath {
	ADDCHILD("/addChild");
	private String path;
	
	public String getUri() {
		return path;
	}
	
	private TestParentControllerPath(String path) {
		this.path = path;
	}
}
