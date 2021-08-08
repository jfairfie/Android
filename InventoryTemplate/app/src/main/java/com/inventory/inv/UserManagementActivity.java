package com.inventory.inv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserManagementActivity extends AppCompatActivity {
    private Button createNewUserBtn, modifyUserActivity;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        //Getting username and password of current user
        Bundle extras = getIntent().getExtras();
        username = extras.getString("Username");
        password = extras.getString("Password");

        //Goes to the CreateNewUser activity
        createNewUserBtn = findViewById(R.id.CreateNewUser);
        createNewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserManagementActivity.this, CreateUserActivity.class);
                intent.putExtra("Username", username);
                intent.putExtra("Password", password);
                startActivity(intent);
            }
        });

        //Goes to ModifyUser Activity
        modifyUserActivity = findViewById(R.id.ModifyUser);
        modifyUserActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserManagementActivity.this, ModifyUserActivity.class);
                intent.putExtra("Username", username);
                intent.putExtra("Password", password);
                startActivity(intent);
            }
        });
    }
}