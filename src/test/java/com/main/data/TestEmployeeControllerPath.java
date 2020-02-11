package com.main.data;

public enum TestEmployeeControllerPath {
    AFTER_SCHOOL_CARES("/api/employee/after_school_cares");

    private String path;

    public String getUri() {
        return path;
    }

    private TestEmployeeControllerPath(String path) {
        this.path = path;
    }
}
