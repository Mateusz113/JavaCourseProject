package com.mateusz113.shop.io.file;

public abstract class FileHandler {
    private final String userLoginInfoPath;
    private final String userDetailsPath;
    private final String productDetailsPath;

    public FileHandler(String userLoginInfoPath, String userDetailsPath, String productDetailsPath) {
        this.userLoginInfoPath = userLoginInfoPath;
        this.userDetailsPath = userDetailsPath;
        this.productDetailsPath = productDetailsPath;
    }

    public String getUserLoginInfoPath() {
        return userLoginInfoPath;
    }

    public String getUserDetailsPath() {
        return userDetailsPath;
    }

    public String getProductDetailsPath() {
        return productDetailsPath;
    }
}
