package com.nicolaslagamma.gammagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
    // Add a new list of tweets
    public void addAll(List<Post> newPosts) {
        posts.addAll(newPosts);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDisplayName;
        private TextView tvDescription;
        private ImageView ivPost;
        private ImageView ivProfilePicture;
        private LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDisplayName = itemView.findViewById(R.id.tvDisplayName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivPost = itemView.findViewById(R.id.ivPost);
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            container = itemView.findViewById(R.id.container);
        }

        // binds the post data to the view element
        public void bind(final Post post) {
            tvDescription.setText(post.getDescription());
            tvDisplayName.setText(post.getUser().getUsername());

            // TODO: swap out for post.getUser().getProfileImage() later
            Glide.with(context)
                    .load(R.mipmap.instagram_user_filled_24)
                    .into(ivProfilePicture);

            final ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivPost);
                ivPost.setVisibility(View.VISIBLE);
            } else {
                ivPost.setVisibility(View.GONE);
            }

            // register click listener on the whole row (container)
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // navigate to a new activity on tap
                    Intent i = new Intent(context, DetailActivity.class);
                    // Pass data object in the bundle and populate details activity.
                    i.putExtra("post", Parcels.wrap(post));
                    Pair<View, String> profilePicture = Pair.create((View)ivProfilePicture, "profilePicture");
                    ActivityOptionsCompat options;
                    if (image != null) {
                        Pair<View, String> postImage = Pair.create((View)ivPost, "postImage");
                        options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, profilePicture, postImage);
                    } else {
                        options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, profilePicture);
                    }
                    context.startActivity(i, options.toBundle());
                }
            });

        }
    }
}
