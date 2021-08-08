package com.inventory.inv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//Class logins the user into the MySQL Database
public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    EditText userInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInput = findViewById(R.id.UserNameInput);
        passwordInput = findViewById(R.id.PasswordInput);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Database database = new Database(MainActivity.this, userInput.getText().toString(), passwordInput.getText().toString());
                database.Login();
            }
        });
    }


}