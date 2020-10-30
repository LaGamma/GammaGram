package com.nicolaslagamma.gammagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nicolaslagamma.gammagram.fragments.ComposeFragment;
import com.nicolaslagamma.gammagram.fragments.PostsFragment;
import com.nicolaslagamma.gammagram.fragments.ProfileFragment;
import com.nicolaslagamma.gammagram.ui.login.LoginActivity;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();
    final Fragment composeFragment = new ComposeFragment();
    final Fragment profileFragment = new ProfileFragment();
    final Fragment homeFragment = new PostsFragment();
    private BottomNavigationView bottomNavigationView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        // Move back to login screen and destroy main activity
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        setResult(Activity.RESULT_OK);
                        Toast.makeText(MainActivity.this, "Successfully logged out!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                return true;
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // check id of item and perform appropriate navigation
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_compose:
                        fragment = composeFragment;
                        break;
                    case R.id.action_profile:
                        fragment = profileFragment;
                        break;
                    case R.id.action_home:
                    default:
                        fragment = homeFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

}