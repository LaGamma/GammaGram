package com.nicolaslagamma.gammagram.fragments;

import com.parse.ParseUser;

public class ProfileFragment extends PostsFragment {

    @Override
    protected void queryPosts(boolean more) {
        fetchPosts(ParseUser.getCurrentUser(), more);
    }
}