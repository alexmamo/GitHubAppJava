package ro.alexmamo.githubapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import ro.alexmamo.githubapp.R;
import ro.alexmamo.githubapp.adapters.RepositoryAdapter;
import ro.alexmamo.githubapp.async_tasks.RepositoryAsyncTask;
import ro.alexmamo.githubapp.models.Repository;

import static ro.alexmamo.githubapp.Constants.BASE_URL;
import static ro.alexmamo.githubapp.Constants.REPOSITORIES_PER_PAGE;
import static ro.alexmamo.githubapp.Constants.SINCE;

public class RepositoriesActivity extends AppCompatActivity {
    private List<Repository> repositoryList = new ArrayList<>();
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositories);
        initViews();
    }

    private void initViews() {
        RecyclerView repositoryRecyclerView = findViewById(R.id.repository_recycler_view);
        RepositoryAdapter repositoryAdapter = new RepositoryAdapter(this, repositoryList);
        repositoryRecyclerView.setAdapter(repositoryAdapter);
        getRepositories(RepositoriesActivity.this, repositoryAdapter, BASE_URL);

        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                if (linearLayoutManager != null) {
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();

                    if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                        isScrolling = false;
                        Repository lastRepository = repositoryList.get(repositoryList.size() - 1);
                        String lastRepositoryId = lastRepository.id;
                        String nextPageUrl = BASE_URL + SINCE + lastRepositoryId;
                        getRepositories(RepositoriesActivity.this, repositoryAdapter, nextPageUrl);

                        if (repositoryList.size() < REPOSITORIES_PER_PAGE) {
                            isLastItemReached = true;
                        }
                    }
                }
            }
        };
        repositoryRecyclerView.addOnScrollListener(onScrollListener);
    }

    private void getRepositories(RepositoriesActivity repositoriesActivity, RepositoryAdapter repositoryAdapter, String url) {
        RepositoryAsyncTask repositoryAsyncTask = new RepositoryAsyncTask(repositoriesActivity, repositoryList, repositoryAdapter);
        repositoryAsyncTask.execute(url);
    }
}