package edu.csumb.nishihara_puzon.login_register;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener {

    Button bLogout;
    EditText etName, etLastName, etEmail, etUsername;

    UserLocalStore userLocalStore;

    private static final int SELECTED_PICTURE=1;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.etName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        bLogout = (Button) findViewById(R.id.bLogout);

        bLogout.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                Intent loginIntent = new Intent(this, Login.class);
                startActivity(loginIntent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authenticate() == true) {
            displayUserDetails();
        }
    }

    private boolean authenticate() {
        if (userLocalStore.getLoggedInUser() == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return false;
        }
        return true;
    }

    private void displayUserDetails() {
        User user = userLocalStore.getLoggedInUser();
        etUsername.setText(user.username);
        etName.setText(user.name);
        //etAge.setText(user.age + "");
    }

}