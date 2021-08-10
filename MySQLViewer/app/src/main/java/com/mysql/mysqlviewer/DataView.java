package com.mysql.mysqlviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

public class DataView extends AppCompatActivity {
    private Map<String, String> map;
    private Database database;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);

        map = new HashMap<String, String>();

        //Initializing variables
            //Getting data from previous activity
        Bundle extras = getIntent().getExtras();
        map.put("username", extras.getString("Username"));
        map.put("password", extras.getString("Password"));
        String address = extras.getString("Address");
            //Initializing widgets
        database = new Database(DataView.this, address);
        listView = findViewById(R.id.mysqlStructure);

        //On create initially Show available databases showDatabases.php

    }
}