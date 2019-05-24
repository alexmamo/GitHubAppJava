package ro.alexmamo.githubapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ro.alexmamo.githubapp.R;
import ro.alexmamo.githubapp.activities.RepositoryActivity;
import ro.alexmamo.githubapp.models.Repository;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {
    private Context context;
    private List<Repository> repositoryList;

    public RepositoryAdapter(Context context, List<Repository> repositoryList) {
        this.context = context;
        this.repositoryList = repositoryList;
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_repository, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder repositoryViewHolder, int position) {
        Repository repository = repositoryList.get(position);
        repositoryViewHolder.bindRepository(repository);
    }

    @Override
    public int getItemCount() {
        return repositoryList.size();
    }

    class RepositoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView idTextView;
        TextView nameTextView;
        TextView fullNameTextView;

        RepositoryViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            idTextView = itemView.findViewById(R.id.id_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            fullNameTextView = itemView.findViewById(R.id.full_name_text_view);
        }

        private void bindRepository(Repository repository) {
            setIdTextView(repository.id);
            setNameTextView(repository.name);
            setIdFullNameView(repository.fullName);
        }

        private void setIdTextView(String id) {
            idTextView.setText(context.getString(R.string.starting_id, id));
        }

        private void setNameTextView(String name) {
            nameTextView.setText(name);
        }

        private void setIdFullNameView(String fullName) {
            fullNameTextView.setText(fullName);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Repository clickedRepository = repositoryList.get(position);
            goToRepositoryActivity(clickedRepository);
        }

        private void goToRepositoryActivity(Repository repository) {
            Intent repositoryActivityIntent = new Intent(context, RepositoryActivity.class);
            repositoryActivityIntent.putExtra("repository", repository);
            context.startActivity(repositoryActivityIntent);
        }
    }
}