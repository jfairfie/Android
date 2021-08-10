package com.inventory.inv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifyUserActivity extends AppCompatActivity {
    private ListView listView, permissionListView;
    private Button modifyBtn;
    private EditText usernameText, tableText, permissionText;
    private Database database;
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);

        //Getting information from other view
        Bundle extras = getIntent().getExtras();
        String username = extras.getString("Username");
        String password = extras.getString("Password");

        //Instantiating Views
        listView = findViewById(R.id.ModifyUserListView);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.0f);
        param.height = 500;
        listView.setLayoutParams(param);

        permissionListView = findViewById(R.id.ModifyUserPermissionListView);
        permissionListView.setLayoutParams(param);

        modifyBtn = findViewById(R.id.ModifyUserModify);
        usernameText = findViewById(R.id.ModifyUserUserName);
        tableText = findViewById(R.id.ModifyUserTableName);
        permissionText = findViewById(R.id.ModifyUserPermission);
        database = new Database(ModifyUserActivity.this, username, password);
        toggleButton = findViewById(R.id.ModifyUserToggleButton);

        //Setting text of listview adapter to usernames list
        String userList = database.GetUserNames();
        List<String> users = null;
        try {
            JSONArray jsonArray = new JSONArray(userList);
            users = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                users.add(jsonObject.getString("username"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModifyUserActivity.this, android.R.layout.simple_list_item_1, users);
        listView.setAdapter(adapter);


        //Modifies User
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameText.getText().toString().trim().equals("") == false && tableText.getText().toString().trim().equals("") == false && permissionText.getText().toString().trim().equals("") == false) {
                    //Getting user input to modify permissions
                    Map map = new HashMap<String, String>();
                    map.put("user", usernameText.getText().toString().trim());
                    map.put("table", tableText.getText().toString().trim());
                    map.put("permission", permissionText.getText().toString().trim());

                    if (toggleButton.isChecked() == false) {
                        database.GrantUserPermission(map);
                    } else if (toggleButton.isChecked() == true) {
                        database.RevokeUserPermission(map);
                    }
                    String json = database.GetPermissionNames(usernameText.getText().toString());
                    List<String> permissionsList = returnList(json);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(ModifyUserActivity.this, android.R.layout.simple_list_item_1, permissionsList);
                    permissionListView.setAdapter(arrayAdapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Setting adapter of ListView
                String item = parent.getItemAtPosition(position).toString();
                String json = database.GetPermissionNames(item.trim());
                List<String> permissionsList = returnList(json);
                usernameText.setText(item.trim());
                ArrayAdapter arrayAdapter = new ArrayAdapter(ModifyUserActivity.this, android.R.layout.simple_list_item_1, permissionsList);
                permissionListView.setAdapter(arrayAdapter);
            }
        });

        permissionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                item = item.substring(0, item.lastIndexOf(" ")).trim();
                item = item.substring(item.lastIndexOf(" ")).trim();
                tableText.setText(item);
            }
        });
    }

    private ArrayList<String> returnList(String json) {
        ArrayList<String> permissionsList = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                String permission = jsonObject.getString("permission");
                permission = removeInsignificantCharacters(permission);
                permissionsList.add(permission.trim());
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return permissionsList;
    }

    //Returns String modified to remove insignificant characters
    private String removeInsignificantCharacters(String input) {
        String permission = input;
        permission = permission.replace("GRANT", "");
        permission = permission.replace("BPTInventory", "");
        permission = permission.replaceAll("[^a-zA-Z0-9]", " ");
        permission = permission.replace("ON", "");
        permission = permission.replace("TO", "");
        permission = permission.replaceAll("  ", " ");
        permission = permission.trim();
        return permission;
    }
}