package ro.alexmamo.githubapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ro.alexmamo.githubapp.R;
import ro.alexmamo.githubapp.async_tasks.DownloadImageTask;
import ro.alexmamo.githubapp.models.Contributor;

public class ContributorAdapter extends RecyclerView.Adapter<ContributorAdapter.ContributorViewHolder> {
    private Context context;
    private List<Contributor> contributorList;

    public ContributorAdapter(Context context, List<Contributor> contributorList) {
        this.context = context;
        this.contributorList = contributorList;
    }

    @NonNull
    @Override
    public ContributorAdapter.ContributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contributor, parent, false);
        return new ContributorAdapter.ContributorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContributorAdapter.ContributorViewHolder contributorViewHolder, int position) {
        Contributor contributor = contributorList.get(position);
        contributorViewHolder.bind(contributor);
    }

    @Override
    public int getItemCount() {
        return contributorList.size();
    }

    class ContributorViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView loginTextView;
        ImageView avatarUrlImageView;

        ContributorViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            loginTextView = itemView.findViewById(R.id.login_text_view);
            avatarUrlImageView = itemView.findViewById(R.id.avatar_url_image_view);
        }

        private void bind(Contributor contributor) {
            setLoginTextView(contributor.login);
            setAvatarUrlImageView(contributor.avatarUrl);
        }

        private void setLoginTextView(String login) {
            loginTextView.setText(login);
        }

        private void setAvatarUrlImageView(String avatarUrl) {
            DownloadImageTask downloadImageTask = new DownloadImageTask(itemView);
            downloadImageTask.execute(avatarUrl);
        }
    }
}