package com.nicolaslagamma.gammagram.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private String success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable String success) {
        this.success = success;
    }

    @Nullable
    String getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}