package com.example.ugo.newsapp;

public class Football {
    private String description;
    private String time;
    private String source;
    private String url;
    Football (String description, String time, String source, String url){
        this.description = description;
        this.time = time;
        this.source = source;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public String getSource() {
        return source;
    }

    /*
     * Returns the website URL to find more information about football.
     */
    public String getUrl() {
        return url;
    }
}
