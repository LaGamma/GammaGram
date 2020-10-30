package com.nicolaslagamma.gammagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nicolaslagamma.gammagram.EndlessRecyclerViewScrollListener;
import com.nicolaslagamma.gammagram.Post;
import com.nicolaslagamma.gammagram.PostsAdapter;
import com.nicolaslagamma.gammagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostsFragment extends Fragment {

    protected static final String TAG = "HomeFragment";

    protected RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    protected SwipeRefreshLayout swipeContainer;

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);

        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching data!");
                queryPosts(false);
            }
        });

        allPosts = new ArrayList<>();
        // 0. create layout for one row in the list
        // 1. create the adapter
        adapter = new PostsAdapter(getContext(), allPosts);
        // 2. create the data source
        // 3. set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // 4. set the layout manager on the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(layoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(rvPosts.getContext(), layoutManager.getOrientation());
        mDividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(rvPosts.getContext(), R.drawable.divider)));
        rvPosts.addItemDecoration(mDividerItemDecoration);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                queryPosts(true);
            }
        };
        // Adds a scroll listener to RecyclerView
        rvPosts.addOnScrollListener(scrollListener);

        queryPosts(false);
    }

    protected void queryPosts(boolean more) {
        fetchPosts(null, more);
    }

    protected void fetchPosts(ParseUser targetUser, final boolean more) {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        if (targetUser != null) {
            query.whereEqualTo(Post.KEY_USER, targetUser);
        }
        if (more) {
            query.whereLessThan(Post.KEY_CREATED_AT, allPosts.get(allPosts.size() - 1).getCreatedAt());
        }
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        // Specify the object id
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post: posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                if (!more) {
                    adapter.clear();
                }
                adapter.addAll(posts);
                // signal refresh has finished
                swipeContainer.setRefreshing(false);
            }
        });
    }

}