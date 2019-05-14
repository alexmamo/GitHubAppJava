package ro.alexmamo.githubapp.models;

public class Contributor {
    public String login, avatarUrl;

    public Contributor(String login, String avatarUrl) {
        this.login = login;
        this.avatarUrl = avatarUrl;
    }
}