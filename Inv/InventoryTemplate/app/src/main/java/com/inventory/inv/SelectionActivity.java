package com.inventory.inv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectionActivity extends AppCompatActivity {
    private Button testBtn, adminBtn;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        adminBtn = findViewById(R.id.ButtonAdmin);
        testBtn = findViewById(R.id.Data);

        Bundle extras = getIntent().getExtras();
        this.username = extras.getString("Username");
        this.password = extras.getString("Password");

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toViewActivity();
            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAdminActivity();
            }
        });


    }

    private void toViewActivity() {
        Intent intent = new Intent(SelectionActivity.this, AddItemActivity.class);
        intent.putExtra("Username", this.username);
        intent.putExtra("Password", this.password);
        startActivity(intent);
    }


    private void toAdminActivity() {
        Intent intent = new Intent(SelectionActivity.this, AdminActivity.class);
        intent.putExtra("Username", this.username);
        intent.putExtra("Password", this.password);
        startActivity(intent);
    }
}