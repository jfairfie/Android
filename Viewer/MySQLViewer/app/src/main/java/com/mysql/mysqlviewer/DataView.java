package com.mysql.mysqlviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

public class DataView extends AppCompatActivity {
    private Map<String, String> map;
    private Database database;
    private ListView listView;
    private StructureManager structureManager;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);

        map = new HashMap<String, String>();

        //Initializing variables
            //Getting data from previous activity
        Bundle extras = getIntent().getExtras();
        map.put("username", extras.getString("username"));
        map.put("password", extras.getString("password"));
        map.put("endpoint", extras.getString("endpoint"));
        String address = extras.getString("Address");
            //Initializing widgets
        database = new Database(DataView.this, address);
        listView = findViewById(R.id.mysqlStructure);
        structureManager = new StructureManager(listView, map, database);
        backBtn = findViewById(R.id.backButton);

        //On create initially Show available databases showDatabases.php
        String[] selectList = {"Database"};

        structureManager.moveUp("", DataView.this, listView);

        //Clicking on item from listview expands to content beneath it
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Getting which item was selected
                String selectedItem = (String) parent.getItemAtPosition(position);
                map.put("selectedItem", selectedItem.trim());
                structureManager.moveUp(selectedItem.trim(), DataView.this, listView);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
                structureManager.moveDown(DataView.this, listView);
            }
        });
    }
}