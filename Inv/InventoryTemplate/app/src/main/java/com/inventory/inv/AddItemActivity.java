package com.inventory.inv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner catSpinner;
    private LinearLayout linearLayout;
    private List<String> categoryList, columnList;
    private List<EditText> editTextList;
    private Button create, search, clear;
    private Database database;
    private ListView listView;
    private String currentSelection = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Bundle extras = getIntent().getExtras();
        String username = extras.getString("Username");
        String password = extras.getString("Password");

        database = new Database(AddItemActivity.this, username, password);

        //Instantiating widgets/views
        catSpinner = (Spinner) findViewById(R.id.catSpinner);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        create = new Button(AddItemActivity.this);
        create.setText("Create New Item");

        search = new Button(AddItemActivity.this);
        search.setText("Search");

        clear = new Button(AddItemActivity.this);
        clear.setText("Clear");

        catSpinner.setSelected(false);
        catSpinner.setOnItemSelectedListener(AddItemActivity.this);

        listView = new ListView(AddItemActivity.this);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 0.0f);
        param.height = 1500;
        listView.setLayoutParams(param);

        //Creating Drop Down Menu
        categoryList = new ArrayList<String>();
        categoryList.add("Select a category");

        //Getting other categories
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddItemActivity.this, android.R.layout.simple_spinner_dropdown_item, categoryList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get value of item selected and get information, id, quantity
                String selectedItem = (String) parent.getItemAtPosition(position);
                String selectedId = selectedItem.substring(0, selectedItem.indexOf(" "));

                selectedItem = selectedItem.trim();
                int quant = Integer.parseInt(selectedItem.substring(selectedItem.lastIndexOf(" ")).trim());

                //Create Popupwindow and display it
                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View popup = inflater.inflate(R.layout.activity_drop, null);

                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                ToggleButton toggleButton = popup.findViewById(R.id.toggleButtonAddSubtract);
                EditText quantityText = popup.findViewById(R.id.updateQuantity);
                Button confirm = popup.findViewById(R.id.confirmButton);
                final PopupWindow popupWindow = new PopupWindow(popup, width, height, true);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                confirm.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        Map map = new HashMap<String, String>();
                        map.put("category", currentSelection);
                        if (toggleButton.isChecked() == false) {
                            if (Integer.parseInt(quantityText.getText().toString().trim()) <= quant) {
                                map.put("quantity", Integer.toString(-1 * Integer.parseInt(quantityText.getText().toString().trim())));
                                map.put("id", selectedId.trim());
                                database.InsertEntry(map);
                            } else {
                                Toast.makeText(AddItemActivity.this, "Error, quantity subtracted is too large", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            map.put("quantity", quantityText.getText().toString().trim());
                            map.put("id", selectedId.trim());
                            database.InsertEntry(map);
                        }

                        String[] attributes = new String[columnList.size() + 1];
                        attributes[attributes.length - 1] = "quantity";
                        for (int i = 0; i < columnList.size(); i++) {
                            attributes[i] = columnList.get(i);
                        }

                        popupWindow.dismiss();
                        database.GetAll(listView, currentSelection, attributes);
                    }
                });
            }
        });

        //Creates new item
        create.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String params = "";
                for (int i = 0; i < editTextList.size(); i++) {
                    if (editTextList.get(i).getText().toString().trim().equals("")) {
                        Toast.makeText(AddItemActivity.this, "Error:: a field contains no data", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    params += editTextList.get(i).getText().toString().trim();
                    if (i < editTextList.size()-1) {
                        params += ",";
                    }
                }
                Map map = new HashMap<String, String>();
                map.put("params", params);
                map.put("category", currentSelection);
                database.Insert(map);

                String[] attributes = new String[columnList.size() + 1];
                attributes[attributes.length - 1] = "quantity";
                for (int i = 0; i < columnList.size(); i++) {
                    attributes[i] = columnList.get(i);
                }

                database.GetAll(listView, currentSelection, attributes);

            }
        });

        //Clears the field and displays, GetAll()
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < editTextList.size(); i++) {
                    editTextList.get(i).setText("");
                }
                String[] attributes = new String[columnList.size() + 1];
                attributes[attributes.length - 1] = "quantity";
                for (int i = 0; i < columnList.size(); i++) {
                    attributes[i] = columnList.get(i);
                }

                database.GetAll(listView, currentSelection, attributes);
            }
        });

        //Searches for items
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keys = "";
                String params = "";
                Map map = new HashMap<String, String>();
                String[] selectList = new String[columnList.size()+1];

                System.out.println(editTextList.size());
                System.out.println(columnList.size());

                for (int i = 0; i < editTextList.size(); i++) {
                    if (editTextList.get(i).getText().toString().equals("") == false) { //The Edit text is not empty
                        keys += columnList.get(i+1) + ",";
                        params += editTextList.get(i).getText().toString().trim() + ",";
                    }
                }

                //Adding items into selectList
                for (int i = 0; i < columnList.size(); i++) {
                    selectList[i] = columnList.get(i);
                }

                selectList[selectList.length-1] = "quantity";

                if (keys.length() > 0) {
                    //Removing last ,
                    keys = keys.substring(0, keys.length() - 1);
                    params = params.substring(0, params.length() - 1);

                    map.put("keys", keys);
                    map.put("category", currentSelection);
                    map.put("params", params);
                    database.GetBy(listView, map, selectList);

                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Getting selected item
        String item = parent.getItemAtPosition(position).toString();
        currentSelection = item;
        Toast.makeText(parent.getContext(), "Switching to " + item, Toast.LENGTH_SHORT).show();
        editTextList = new ArrayList<EditText>();

        //Create EditText widgets
        if (item.equals(categoryList.get(0)) == false) {
            linearLayout.removeAllViews(); //Ensuring the layout is cleared

            //Adding buttons to layout
            linearLayout.addView(create);
            linearLayout.addView(search);
            linearLayout.addView(clear);

            //Getting Column names as json
            String columnNames = database.GetColumnNames(currentSelection);
            System.out.println(columnNames);
            //Getting list of column names from json array
            columnList = new ArrayList<String>();
            try {
                //Getting regular text from json
                JSONArray jsonArray = new JSONArray(columnNames);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    columnList.add(jsonObject.getString("columnname"));
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }

            //Creating number of views from ColumnList
            for (int i = 1; i < columnList.size(); i++) {
                EditText editText = new EditText(parent.getContext());
                editText.setHint(columnList.get(i));
                editTextList.add(editText);
                linearLayout.addView(editTextList.get(i-1));
            }

            String[] attributes = new String[columnList.size() + 1];
            attributes[attributes.length - 1] = "quantity";
            for (int i = 0; i < columnList.size(); i++) {
                attributes[i] = columnList.get(i);
            }

            linearLayout.addView(listView);
            database.GetAll(listView, currentSelection, attributes);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}