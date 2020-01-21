package com.main.data;

public enum TestSchoolCoordinatorPath {
    GET_SCHOOL("/api/sc/ag"), GET_SCHOOLS("/api/sc/ags");

    private String path;

    public String getUri() {
        return path;
    }

    private TestSchoolCoordinatorPath(String path) {
        this.path = path;
    }

}
