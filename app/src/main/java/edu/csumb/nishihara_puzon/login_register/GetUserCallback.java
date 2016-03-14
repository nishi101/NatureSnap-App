package edu.csumb.nishihara_puzon.login_register;

/**
 * Created by Nick on 3/14/16.
 */
public interface GetUserCallback {

    // When background tasks are complete
    public abstract void done(User returnedUser);
}
