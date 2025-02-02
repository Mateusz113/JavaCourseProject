package com.mateusz113.shop.io.file;

public abstract class FileHandler {
    private final String userLoginInfoPath;
    private final String userDetailsPath;

    public FileHandler(String userLoginInfoPath, String userDetailsPath) {
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
