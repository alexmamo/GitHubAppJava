package ro.alexmamo.githubapp.models;

public class Repo {
    public int size, stargazersCount, forksCount;

    public Repo(int size, int stargazersCount, int forksCount) {
        this.size = size;
        this.stargazersCount = stargazersCount;
        this.forksCount = forksCount;
    }
}