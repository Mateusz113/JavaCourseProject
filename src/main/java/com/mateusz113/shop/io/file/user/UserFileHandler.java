package com.mateusz113.shop.io.file.user;

public abstract class UserFileHandler {
    private final String userLoginInfoPath;
    private final String userDetailsPath;

    public UserFileHandler(String userLoginInfoPath, String userDetailsPath) {
        this.userLoginInfoPath = userLoginInfoPath;
        this.userDetailsPath = userDetailsPath;
    }

    public String getUserLoginInfoPath() {
        return userLoginInfoPath;
    }

    public String getUserDetailsPath() {
        return userDetailsPath;
    }
}
