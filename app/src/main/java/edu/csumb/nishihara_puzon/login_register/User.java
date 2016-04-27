package edu.csumb.nishihara_puzon.login_register;

import java.io.Serializable;

/**
 * Created by Nick on 3/13/16.
 */
public class User implements Serializable {

    String id, name, lastname, email, username, password;

    public User(String id, String name, String lastname, String email, String username, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(String username, String password) {
        this("", "", "", "", username, password);
    }
}
