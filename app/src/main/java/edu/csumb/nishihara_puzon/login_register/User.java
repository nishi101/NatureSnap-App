package edu.csumb.nishihara_puzon.login_register;

/**
 * Created by Nick on 3/13/16.
 */
public class User {

    String name, lastname, email, username, password;

    public User(String name, String lastname, String email, String username, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this("", "", "", username, password);
    }
}
