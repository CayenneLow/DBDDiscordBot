package com.khye.DTO;

import java.util.Objects;

public class RedditPost {
    private String id;
    private String permalink;

    public RedditPost(String id, String permalink) {
        this.id = id;
        this.permalink = permalink;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermalink() {
        return this.permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RedditPost)) {
            return false;
        }
        RedditPost redditPost = (RedditPost) o;
        return Objects.equals(id, redditPost.id) && Objects.equals(permalink, redditPost.permalink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, permalink);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", permalink='" + getPermalink() + "'" +
            "}";
    }
    
}