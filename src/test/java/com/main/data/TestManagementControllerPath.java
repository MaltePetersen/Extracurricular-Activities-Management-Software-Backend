package com.main.data;

public enum TestManagementControllerPath {
    GET_ALL_USERS("/api/management/users"), GET_ATTENDANCE_LIST("/api/management/list");

    private String path;

    public String getUri() {
        return path;
    }

    TestManagementControllerPath(String path) {
        this.path = path;
    }
}