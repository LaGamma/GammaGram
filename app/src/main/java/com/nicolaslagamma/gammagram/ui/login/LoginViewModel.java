package com.nicolaslagamma.gammagram.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.nicolaslagamma.gammagram.data.LoginRepository;
import com.nicolaslagamma.gammagram.data.Result;
import com.nicolaslagamma.gammagram.data.model.LoggedInUser;
import com.nicolaslagamma.gammagram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    // Compiled ReGex to check if a string
    // contains uppercase, lowercase, and numeric value
    private Pattern p = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(final String username, final String password) {
        // launch in a separate asynchronous job
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    if (user.getBoolean("emailVerified")) {
                        // Hooray! The user is logged in.
                        Result<LoggedInUser> result = loginRepository.login(username, password);
                        LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                        loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                    } else {
                        // User did not confirm the e-mail!! (Let them log in anyway)
                        Result<LoggedInUser> result = loginRepository.login(username, password);
                        LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                        loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                    }
                } else {
                    createNewUser(username, password);
                }
            }
        });
    }

    public void createNewUser(final String username, final String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        //user.setEmail(userName);

        // Other fields can be set just like any other ParseObject,
        // using the "put" method, like this: user.put("attribute", "its value");
        // If this field does not exists, it will be automatically created
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Log.i("LoginViewModel", "new user " + username + " created!");
                    Result<LoggedInUser> result = loginRepository.login(username, password);
                    LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Log.e("LoginViewModel", e.toString());
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                    return;
                }
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // must contain a capital letter, lowercase letter, a number and be at least 8 characters long
    private boolean isPasswordValid(String password) {
        return password != null && p.matcher(password).matches() && password.trim().length() > 8;
    }
}