package com.mysql.mysqlviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private List<EditText> list;
    private Button login;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        list = new ArrayList<EditText>();

        //Initializing Button and EditTexts
        login = findViewById(R.id.loginButton);
        list.add(findViewById(R.id.editTextHost)); //Host of MySQL server
        list.add(findViewById(R.id.editTextUsername));
        list.add(findViewById(R.id.editTextPassword));
        list.add(findViewById(R.id.editTextPHPAddress)); //Url for location of php scripts

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(LoginActivity.this, list)) {
                    //Authentication action
                    database = new Database(LoginActivity.this, list.get(3).getText().toString());
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("endpoint", list.get(0).getText().toString());
                    map.put("username", list.get(1).getText().toString());
                    map.put("password", list.get(2).getText().toString());
                    database.Login(map);
                }
            }
        });
    }

    //Checking if EditTexts are empty
    private boolean isEmpty(Context c, List<EditText> list) {
        for (int i = 0; i < list.size(); i++){
            if (TextUtils.isEmpty(list.get(i).getText().toString())) {
                Toast.makeText(c, "Error:: missing value from field ", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
}