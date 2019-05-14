package ro.alexmamo.githubapp;

public class Constants {
    public static final String TAG = "GitHubAppTag";
    private static final String CLIENT_ID = "9ba4f9c12fa92045aa29";
    private static final String CLIENT_SECRET = "816f71bd25f5b0e6890ee0532ac8e77c5c9ca581";
    public static final String SECURITY = "?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET;
    public static final String BASE_URL = "https://api.github.com/repositories" + SECURITY;
    public static final String SINCE = "&since=";
    public static final String CONTRIBUTORS = "/contributors";
    public static final int REPOSITORIES_PER_PAGE = 25;
}