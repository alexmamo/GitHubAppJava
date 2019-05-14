package ro.alexmamo.githubapp.async_tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import ro.alexmamo.githubapp.R;
import ro.alexmamo.githubapp.activities.RepositoriesActivity;
import ro.alexmamo.githubapp.activities.RepositoryActivity;

import static ro.alexmamo.githubapp.Constants.TAG;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private WeakReference<View> weakReference;

    public DownloadImageTask(View itemView) {
        weakReference = new WeakReference<>(itemView);
    }

    @Override
    protected void onPreExecute() {
        showProgressBar();
    }

    protected Bitmap doInBackground(String... urls) {
        String avatarUrl = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(avatarUrl).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        View itemView = weakReference.get();
        if (itemView != null) {
            ImageView avatarUrlImageView = itemView.findViewById(R.id.avatar_url_image_view);
            avatarUrlImageView.setImageBitmap(result);
            hideProgressBar();
        }
    }

    private void showProgressBar() {
        View itemView = weakReference.get();
        ProgressBar progressBar = itemView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        View itemView = weakReference.get();
        ProgressBar progressBar = itemView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }
}