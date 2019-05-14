package ro.alexmamo.githubapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ro.alexmamo.githubapp.R;
import ro.alexmamo.githubapp.adapters.ContributorAdapter;
import ro.alexmamo.githubapp.async_tasks.ContributorAsyncTask;
import ro.alexmamo.githubapp.async_tasks.RepoAsyncTask;
import ro.alexmamo.githubapp.models.Contributor;
import ro.alexmamo.githubapp.models.Repository;

import static ro.alexmamo.githubapp.Constants.CONTRIBUTORS;
import static ro.alexmamo.githubapp.Constants.SECURITY;

public class RepositoryActivity extends AppCompatActivity {
    private List<Contributor> contributorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        enableHomeButton();
        Repository repository = getRepositoryFromIntent();
        setTitle(repository.name);
        getRepo(repository.url);
        initViews(repository.url);
    }

    private void enableHomeButton() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private Repository getRepositoryFromIntent() {
        return (Repository) getIntent().getSerializableExtra("repository");
    }

    private void getRepo(String url) {
        String repoUrl = url + SECURITY;
        RepoAsyncTask repoAsyncTask = new RepoAsyncTask(RepositoryActivity.this);
        repoAsyncTask.execute(repoUrl);
    }

    private void initViews(String url) {
        RecyclerView contributorsRecyclerView = findViewById(R.id.contributors_recycler_view);
        contributorsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        ContributorAdapter contributorAdapter = new ContributorAdapter(this, contributorList);
        contributorsRecyclerView.setAdapter(contributorAdapter);
        getContributors(contributorAdapter, url);
    }

    private void getContributors(ContributorAdapter contributorAdapter, String url) {
        String contributorsUrl = url + CONTRIBUTORS + SECURITY;
        ContributorAsyncTask contributorAsyncTask = new ContributorAsyncTask(RepositoryActivity.this, contributorList, contributorAdapter);
        contributorAsyncTask.execute(contributorsUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}