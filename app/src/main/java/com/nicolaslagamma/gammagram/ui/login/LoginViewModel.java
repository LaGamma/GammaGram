package com.nicolaslagamma.gammagram.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import com.nicolaslagamma.gammagram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    // Compiled ReGex to check if a string
    // contains uppercase, lowercase, and numeric value
    private Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");

    public LoginViewModel() { }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(final String email, final String password) {
        // launch in a separate asynchronous job
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    if (user.getBoolean("emailVerified")) {
                        // Hooray! The user is logged in.
                        loginResult.setValue(new LoginResult(ParseUser.getCurrentUser().getUsername(), 2));
                    } else {
                        // User did not confirm the e-mail!!
                        ParseUser.logOut();
                        loginResult.setValue(new LoginResult("Please verify your email first.", 0));
                    }
                } else {
                    ParseUser.logOut();
                    loginResult.setValue(new LoginResult(e.getMessage() + " Please re-try", 0));
                }
            }
        });
    }

    public void createNewUser(final String email, final String password) {
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(email.substring(0, email.indexOf("@")));

        // Other fields can be set just like any other ParseObject,
        // using the "put" method, like this: user.put("attribute", "its value");
        // If this field does not exists, it will be automatically created
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Ask to verify email before logging in
                    Log.i("LoginViewModel", "new user " + email + " created!");
                    ParseUser.logOut();
                    loginResult.setValue(new LoginResult("Please verify your email before logging in.", 1));
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    ParseUser.logOut();
                    loginResult.setValue(new LoginResult("Account could not be created" + " :" + e.getMessage(), 0));
                }
            }
        });
    }

    public void loginDataChanged(String email, String password) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder email validation check
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // must contain a capital letter, lowercase letter, a number and be at least 8 characters long
    private boolean isPasswordValid(String password) {
        return password != null && p.matcher(password).matches() && password.trim().length() > 8;
    }
}