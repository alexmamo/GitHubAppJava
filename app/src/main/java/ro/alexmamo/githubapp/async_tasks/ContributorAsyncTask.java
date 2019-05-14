package ro.alexmamo.githubapp.async_tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ro.alexmamo.githubapp.R;
import ro.alexmamo.githubapp.activities.RepositoriesActivity;
import ro.alexmamo.githubapp.activities.RepositoryActivity;
import ro.alexmamo.githubapp.adapters.ContributorAdapter;
import ro.alexmamo.githubapp.adapters.RepositoryAdapter;
import ro.alexmamo.githubapp.models.Contributor;
import ro.alexmamo.githubapp.models.Repository;

import static ro.alexmamo.githubapp.Constants.REPOSITORIES_PER_PAGE;
import static ro.alexmamo.githubapp.Constants.TAG;

public class ContributorAsyncTask extends AsyncTask<String, Void, List<Contributor>> {
    private WeakReference<RepositoryActivity> weakReference;
    private List<Contributor> contributorList;
    private ContributorAdapter contributorAdapter;

    public ContributorAsyncTask(RepositoryActivity repositoryActivity,
                                List<Contributor> contributorList,
                                ContributorAdapter contributorAdapter) {
        weakReference = new WeakReference<>(repositoryActivity);
        this.contributorList = contributorList;
        this.contributorAdapter = contributorAdapter;
    }

    @Override
    protected void onPreExecute() {
        showProgressBar();
    }

    @Override
    protected List<Contributor> doInBackground(String... urls) {
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
            JSONArray jsonArray = new JSONArray(result.toString());
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String login = jsonObject.getString("login");
                String avatarUrl = jsonObject.getString("avatar_url");
                Contributor contributor = new Contributor(login, avatarUrl);
                contributorList.add(contributor);
            }
            bufferedReader.close();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return contributorList;
    }

    @Override
    protected void onPostExecute(List<Contributor> contributorList) {
        contributorAdapter.notifyDataSetChanged();
        hideProgressBar();
    }

    private void showProgressBar() {
        RepositoryActivity repositoryActivity = weakReference.get();
        ProgressBar progressBar = repositoryActivity.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        RepositoryActivity repositoryActivity = weakReference.get();
        ProgressBar progressBar = repositoryActivity.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }
}