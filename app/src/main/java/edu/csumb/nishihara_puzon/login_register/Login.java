package edu.csumb.nishihara_puzon.login_register;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.naturesnap.apiclient.Interface;

import net.naturesnap.apiclient.http.enums.Format;
import net.naturesnap.apiclient.http.enums.Type;
import net.naturesnap.apiclient.http.results.Code;
import net.naturesnap.apiclient.http.results.UserResponse;


public class Login extends Activity implements View.OnClickListener {

    Button bLogin;
    TextView registerLink;
    EditText etUsername, etPassword;

//    public Login() {
//        this.setEndpoint("login.php");
//        this.setFormat(Format.CODE);
//        this.setParams("username", "password");
//        this.setType(Type.POST);
//        this.setResult(Code.class);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UserLocalStore.user != null) {
            logUserIn(UserLocalStore.user);
            return;
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);

        bLogin = (Button) findViewById(R.id.bLogin);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        registerLink = (TextView) findViewById(R.id.tvRegisterLink);

        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User(username, password);

                authenticate(user);
                break;
            case R.id.tvRegisterLink:
                Intent registerIntent = new Intent(Login.this, Register.class);
                startActivity(registerIntent);
                break;
        }
    }

    private void authenticate(User user) {
        UserResponse loginResponse = (UserResponse) Interface.request(new net.naturesnap.apiclient.http.requests.Login(), new String[]{user.username, user.password});
        if (!loginResponse.getSuccess()) {
            showErrorMessage();
        } else if (loginResponse.getSuccess()) {
            user.id = loginResponse.getUser_id();
            logUserIn(user);
        }
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect user information");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser) {
        UserLocalStore.user = returnedUser;
        startActivity(new Intent(this, MainActivity.class));
    }
}
