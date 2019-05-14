package ro.alexmamo.githubapp.async_tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import ro.alexmamo.githubapp.R;
import ro.alexmamo.githubapp.activities.RepositoryActivity;
import ro.alexmamo.githubapp.models.Repo;

import static ro.alexmamo.githubapp.Constants.TAG;

public class RepoAsyncTask extends AsyncTask<String, Void, Repo> {
    private WeakReference<RepositoryActivity> weakReference;

    public RepoAsyncTask(RepositoryActivity repositoriesActivity) {
        weakReference = new WeakReference<>(repositoriesActivity);
    }

    @Override
    protected Repo doInBackground(String... urls) {
        Repo repo = null;

        if (urls.length < 1 || urls[0] == null) {
            return null;
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urls[0]).openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder result = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null) {
                result.append("\n").append(line);
            }
            JSONObject jsonObject = new JSONObject(result.toString());
            int size = jsonObject.getInt("size");
            int stargazersCount = jsonObject.getInt("stargazers_count");
            int forksCount = jsonObject.getInt("forks_count");
            repo = new Repo(size, stargazersCount, forksCount);
            bufferedReader.close();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return repo;
    }

    @Override
    protected void onPostExecute(Repo repo) {
        setSizeTextView(repo);
        setStargazersCountTextView(repo);
        setForksCountTextView(repo);
    }

    private void setSizeTextView(Repo repo) {
        RepositoryActivity repositoryActivity = weakReference.get();
        TextView sizeTextView = repositoryActivity.findViewById(R.id.size_text_view);
        String size = "Size of the repository: " + repo.size;
        sizeTextView.setText(size);
    }

    private void setStargazersCountTextView(Repo repo) {
        RepositoryActivity repositoryActivity = weakReference.get();
        TextView sizeTextView = repositoryActivity.findViewById(R.id.stargazers_count_text_view);
        String stargazersCount = "Stargazers Count: " + repo.stargazersCount;
        sizeTextView.setText(stargazersCount);
    }

    private void setForksCountTextView(Repo repo) {
        RepositoryActivity repositoryActivity = weakReference.get();
        TextView sizeTextView = repositoryActivity.findViewById(R.id.forks_count_text_view);
        String forksCount = "Forks Count: " + repo.forksCount;
        sizeTextView.setText(forksCount);
    }
}