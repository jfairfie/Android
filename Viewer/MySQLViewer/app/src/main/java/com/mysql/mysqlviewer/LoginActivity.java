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
    private Button login, createProfile, openProfile, clearProfiles;
    private Database database;
    private LoginAuthentication loginAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        list = new ArrayList<EditText>();

        //Initializing Button and EditTexts
        login = findViewById(R.id.loginButton);
        openProfile = findViewById(R.id.openProfileButton);
        createProfile = findViewById(R.id.createProfileButton);
        clearProfiles = findViewById(R.id.clearProfilesButton);
        list.add(findViewById(R.id.editTextHost)); //Host of MySQL server
        list.add(findViewById(R.id.editTextUsername));
        list.add(findViewById(R.id.editTextPassword));
        list.add(findViewById(R.id.editTextPHPAddress)); //Url for location of php scripts

        loginAuthentication = new LoginAuthentication(LoginActivity.this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEmpty(LoginActivity.this, list)) {
                    //Authentication action
                    String request = list.get(3).getText().toString().trim();
                    database = new Database(LoginActivity.this, request);

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("endpoint", list.get(0).getText().toString());
                    map.put("username", list.get(1).getText().toString());
                    map.put("password", list.get(2).getText().toString());
                    database.Login(map);
                }
            }
        });

        //Creates a new profile
        createProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!isEmpty(LoginActivity.this, list)) {
                    //keys: username, endpoint, address
                    String[] data = {list.get(1).getText().toString(), list.get(0).getText().toString(), list.get(3).getText().toString()};
                    loginAuthentication.createNewProfile(data);
                }
            }
        });

        //Opens up a profile
        openProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String[] dataList = loginAuthentication.returnProfile();

                //Username, endpoint, address
                list.get(0).setText(dataList[1]); //Setting endpoint
                list.get(1).setText(dataList[0]); //Setting username
                list.get(3).setText(dataList[2]); //Setting address
            }
        });

        clearProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAuthentication.removeProfiles();
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