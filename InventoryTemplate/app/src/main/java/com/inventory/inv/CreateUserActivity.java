package com.inventory.inv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class CreateUserActivity extends AppCompatActivity {
    private String username, password;
    private Button addUserBtn;
    private EditText usernameText, passwordText, nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        //Getting information of current user
        Bundle extras = getIntent().getExtras();
        username = extras.getString("Username");
        password = extras.getString("Password");

        //Instantiating Views
        addUserBtn = findViewById(R.id.buttonAddUser);
        nameText = findViewById(R.id.editTextTextPersonName);
        passwordText = findViewById(R.id.editTextTextPassword);
        usernameText = findViewById(R.id.editTextTextUsername);


        Database database = new Database(CreateUserActivity.this, username, password);

        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = usernameText.getText().toString();
                String pass = passwordText.getText().toString();
                Map map = new HashMap<String, String>();
                map.put("newUser", user);
                map.put("newPass", pass);
                if (nameText.getText().toString() != null) {
                    map.put("name", nameText.getText().toString());
                }

                database.AddUser(map);
            }
        });

    }
}