package com.nicolaslagamma.gammagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // Register parse models
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("U5V9OK3KhXbJfroH8HxgW9nLsJz000F1W6kDqqTM")
                .clientKey("U9HXQW8OHnj3z1zZkffQJot3axrMDuX1gcmHv82a")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
