package com.nicolaslagamma.gammagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("U5V9OK3KhXbJfroH8HxgW9nLsJz000F1W6kDqqTM")
                .clientKey("U9HXQW8OHnj3z1zZkffQJot3axrMDuX1gcmHv82a")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
