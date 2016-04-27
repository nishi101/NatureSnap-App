package edu.csumb.nishihara_puzon.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.naturesnap.apiclient.Interface;

public class Register extends Activity implements View.OnClickListener {

    Button bRegister;
    EditText etName, etLastName, etEmail, etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegister:
                String name = etName.getText().toString();
                String lastname = etLastName.getText().toString();
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                //int age = Integer.parseInt(etAge.getText().toString());

                User user = new User("", name, lastname, email, username, password);

                registerUser(user);
                break;
        }
    }

    private void registerUser(User user) {
        String registerResponse = Interface.apiRequest(new net.naturesnap.apiclient.http.requests.Register(), new String[]{user.name, user.lastname, user.email, user.username, user.password});
        if (registerResponse.equals("success")) {
            Intent loginIntent = new Intent(Register.this, Login.class);
            startActivity(loginIntent);
        } else if (registerResponse.equals("exists")) {

        } else if (registerResponse.equals("invalid")) {

        }
    }

}
