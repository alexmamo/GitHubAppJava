package ro.alexmamo.githubapp.models;

import java.io.Serializable;

public class Repository implements Serializable {
    public String id, name, fullName, url;

    public Repository(String id, String name, String fullName, String url) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.url = url;
    }
}