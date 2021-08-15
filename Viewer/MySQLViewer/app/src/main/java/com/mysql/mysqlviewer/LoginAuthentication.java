package com.mysql.mysqlviewer;

import android.content.Context;
import android.widget.Toast;

public class LoginAuthentication {
    DataManager dataManager;
    Context c;

    public LoginAuthentication(Context c) {
        dataManager = new DataManager(c);
        this.c = c;
    }

    //Authenticates the login/profile
    public boolean authenticate() {
        return false;
    }

    public void createNewProfile(String data, String fileName) {
        dataManager.writeFile(fileName, data);
        Toast.makeText(c, "Created New Profile", Toast.LENGTH_SHORT).show();
    }

    public void getProfiles() {
        String[] retFiles = dataManager.returnFiles();
    }
}
