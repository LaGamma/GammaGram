package com.nicolaslagamma.gammagram.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private String success;
    @Nullable
    private String verify;
    @Nullable
    private String error;

    LoginResult(@Nullable String result, int success)
    {
        if (success == 0) {
            this.error = result;
        } else if (success == 1) {
            this.verify = result;
        } else {
            this.success = result;
        }
    }

    @Nullable
    String getSuccess() {
        return success;
    }

    @Nullable
    String getError() {
        return error;
    }

    @Nullable
    String getVerify() {
        return verify;
    }
}