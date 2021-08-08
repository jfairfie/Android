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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button create, addColumn, removeColumn;
    private Spinner catSpinner;
    private EditText categoryName;
    private ArrayList<EditText> editTextList;

    private LinearLayout categoryManagement;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoryManagement = (LinearLayout) findViewById(R.id.cat_linear_layout);
        Bundle extras = getIntent().getExtras();

        String username = extras.getString("Username");
        String password = extras.getString("Password");

        catSpinner = findViewById(R.id.categorySpinner);
        catSpinner.setOnItemSelectedListener(CategoryActivity.this);

        ArrayList<String> catList = new ArrayList<String>();
        catList.add("Create Category");
        catList.add("Remove Category");
        catList.add("Modify Category");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CategoryActivity.this, android.R.layout.simple_spinner_dropdown_item, catList);
        catSpinner.setAdapter(adapter);

        database = new Database(CategoryActivity.this, username, password);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categoryManagement.removeAllViews();

        if (position == 0) { //Create category
            createCategory();
        } else if (position == 1) { //Remove category
            removeCategory();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Removes a category
    private void removeCategory() {
        //Adding views to linearlayout
        categoryName = new EditText(CategoryActivity.this);
        categoryName.setHint("Category Name");
        categoryManagement.addView(categoryName);

        create = new Button(CategoryActivity.this);
        create.setText("Remove");
        categoryManagement.addView(create);

        ListView listView = new ListView(CategoryActivity.this);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.0f);
        param.height = 1500;
        listView.setLayoutParams(param);
        categoryManagement.addView(listView);

        setListCategory(listView);

        //Removes Category
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryName.getText().toString().trim().equals("") == false) {
                    Map map = new HashMap<String, String>();
                    map.put("category", categoryName.getText().toString().trim());
                    database.RemoveCategory(map);
                    setListCategory(listView);
                }
            }
        });
    }

    //Creates a new category
    private void createCategory() {
        editTextList = new ArrayList<EditText>();

        //Adding views activity_category.xml
        create = new Button(CategoryActivity.this);
        create.setText("Create New Category");
        categoryManagement.addView(create);

        categoryName = new EditText(CategoryActivity.this);
        categoryName.setHint("Category Name");
        categoryManagement.addView(categoryName);

        addColumn = new Button(CategoryActivity.this);
        addColumn.setText("Add New Column");
        categoryManagement.addView(addColumn);

        removeColumn = new Button(CategoryActivity.this);
        removeColumn.setText("Remove Column");
        categoryManagement.addView(removeColumn);

        ListView listView = new ListView(CategoryActivity.this);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.0f);
        param.height = 450;
        listView.setLayoutParams(param);
        categoryManagement.addView(listView);

        setListCategory(listView);

        //Removes Edittext row from view
        removeColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextList.size() > 0) {
                    categoryManagement.removeView(editTextList.get(editTextList.size()-1));
                    editTextList.remove(editTextList.size()-1);
                }
            }
        });

        //Creates a new category
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map = new HashMap<String, String>();

                String field = "";
                //Creating fields for CREATE TABLE
                for (int i = 0; i < editTextList.size(); i++) {
                    if (editTextList.get(i).getText().toString().equals("")) {
                        Toast.makeText(CategoryActivity.this, "Error: a field is missing data", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    field += editTextList.get(i).getText().toString().trim();
                    if (i < editTextList.size()-1) {
                        field += ",";
                    }
                }

                if (categoryName.getText().toString().trim().equals("")) {
                    Toast.makeText(CategoryActivity.this, "Error: there is no category name, please enter one...", Toast.LENGTH_SHORT).show();
                    return;
                }

                map.put("fields", field);
                map.put("category", categoryName.getText().toString());

                database.InsertCategory(map);
                setListCategory(listView);
            }
        });

        //Adds a edittext to the row
        addColumn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(CategoryActivity.this);
                editText.setHint("(Column Name) (Datatype)");
                editTextList.add(editText);
                categoryManagement.addView(editText);
            }
        });
    }

    private void setListCategory(ListView listView) {
        List<String> categoryList = new ArrayList<String>();

        String tableNames = database.ReturnCategoryNames();
        try {
            JSONArray jsonArray = new JSONArray(tableNames);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                categoryList.add(jsonObject.getString("tablename"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(CategoryActivity.this, android.R.layout.simple_list_item_1, categoryList);
        listView.setAdapter(catAdapter);
    }
}