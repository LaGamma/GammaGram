package com.nicolaslagamma.gammagram.data;

import com.nicolaslagamma.gammagram.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {
        try {
            // (doesn't really) handle loggedInUser authentication
            LoggedInUser user = new LoggedInUser(username, username);
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}