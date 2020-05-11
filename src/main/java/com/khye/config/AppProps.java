package com.khye.config;

public class AppProps {
    private int defaultNMemes;
    private int maxMemes;
    private String prefix;

    public int getDefaultNMemes() {
        return this.defaultNMemes;
    }

    public void setDefaultNMemes(int defaultNMemes) {
        this.defaultNMemes = defaultNMemes;
    }

    public int getMaxMemes() {
        return this.maxMemes;
    }

    public void setMaxMemes(int maxMemes) {
        this.maxMemes = maxMemes;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
