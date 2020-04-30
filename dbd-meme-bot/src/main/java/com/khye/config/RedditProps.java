package com.khye.config;

public class RedditProps {
    private String base;
    private String oauthBase;
    private String memeSource;
    private String refreshToken;
    private String authHeader;
    private String refreshTokenUrl;

    public String getBase() {
        return this.base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getOauthBase() {
        return this.oauthBase;
    }

    public void setOauthBase(String oauthBase) {
        this.oauthBase = oauthBase;
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
