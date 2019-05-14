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
import ro.alexmamo.githubapp.adapters.RepositoryAdapter;
import ro.alexmamo.githubapp.models.Repository;

import static ro.alexmamo.githubapp.Constants.REPOSITORIES_PER_PAGE;
import static ro.alexmamo.githubapp.Constants.TAG;

public class RepositoryAsyncTask extends AsyncTask<String, Void, List<Repository>> {
    private WeakReference<RepositoriesActivity> weakReference;
    private List<Repository> repositoryList;
    private RepositoryAdapter repositoryAdapter;

    public RepositoryAsyncTask(RepositoriesActivity repositoriesActivity, List<Repository> repositoryList, RepositoryAdapter repositoryAdapter) {
        weakReference = new WeakReference<>(repositoriesActivity);
        this.repositoryList = repositoryList;
        this.repositoryAdapter = repositoryAdapter;
    }

    @Override
    protected void onPreExecute() {
        showProgressBar();
    }

    @Override
    protected List<Repository> doInBackground(String... urls) {
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
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String fullName = jsonObject.getString("full_name");
                String url = jsonObject.getString("url");
                Repository repository = new Repository(id, name, fullName, url);
                repositoryList.add(repository);

                if(i == REPOSITORIES_PER_PAGE - 1) {
                    break;
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return repositoryList;
    }

    @Override
    protected void onPostExecute(List<Repository> repositoryList) {
        repositoryAdapter.notifyDataSetChanged();
        hideProgressBar();
    }

    private void showProgressBar() {
        RepositoriesActivity repositoriesActivity = weakReference.get();
        ProgressBar progressBar = repositoriesActivity.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        RepositoriesActivity repositoriesActivity = weakReference.get();
        ProgressBar progressBar = repositoriesActivity.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }
}