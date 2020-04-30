package com.khye.config;

public class RedditProps {
    private String baseApi;
    private String memeSource;
    private String refreshToken;
    private String authHeader;
    private String refreshTokenUrl;

    public String getBaseApi() {
        return this.baseApi;
    }

    public void setBaseApi(String baseApi) {
        this.baseApi = baseApi;
    }

    public String getMemeSource() {
        return this.memeSource;
    }

    public void setMemeSource(String memeSource) {
        this.memeSource = memeSource;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAuthHeader() {
        return this.authHeader;
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }

    public String getRefreshTokenUrl() {
        return this.refreshTokenUrl;
    }

    public void setRefreshTokenUrl(String refreshTokenUrl) {
        this.refreshTokenUrl = refreshTokenUrl;
    }

}
