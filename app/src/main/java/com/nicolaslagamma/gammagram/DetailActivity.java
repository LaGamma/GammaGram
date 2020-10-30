package com.nicolaslagamma.gammagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    TextView tvDesc;
    TextView tvName;
    TextView tvPostTime;
    ImageView ivPost;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvDesc = findViewById(R.id.tvDesc);
        tvName = findViewById(R.id.tvDisplayName);
        tvPostTime = findViewById(R.id.tvPostTime);
        ivPost = findViewById(R.id.ivPost);
        ivProfileImage = findViewById(R.id.ivProfilePicture);

        final Post post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        tvDesc.setText(post.getDescription());
        tvName.setText(post.getUser().getUsername());
        tvPostTime.setText(post.getCreatedAt().toString());

        // TODO: swap out for post.getUser().getProfileImage() later
        Glide.with(getApplicationContext())
                .load(R.mipmap.instagram_user_filled_24)
                .into(ivProfileImage);

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this)
                    .load(image.getUrl())
                    .into(ivPost);
            ivPost.setVisibility(View.VISIBLE);
        } else {
            ivPost.setVisibility(View.GONE);
        }
    }
}