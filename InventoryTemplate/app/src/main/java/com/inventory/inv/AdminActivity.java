package com.inventory.inv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminActivity extends AppCompatActivity {
    private Button createCategoryBtn, userBtn;
    private EditText usernameText, passwordText, nameText;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2);

        createCategoryBtn = findViewById(R.id.CreateCategory);
        usernameText = findViewById(R.id.editTextTextPersonName);
        passwordText = findViewById(R.id.editTextTextPassword);
        nameText = findViewById(R.id.editTextTextUsername);
        userBtn = findViewById(R.id.UserActivity);

        //Getting username and password of current user
        Bundle extras = getIntent().getExtras();
        username = extras.getString("Username");
        password = extras.getString("Password");


        createCategoryBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, CategoryActivity.class);
                intent.putExtra("Username", username);
                intent.putExtra("Password", password);
                startActivity(intent);
            }
        });

        userBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, UserManagementActivity.class);
                intent.putExtra("Username", username);
                intent.putExtra("Password", password);
                startActivity(intent);
            }
        });

    }
}