package com.mysql.mysqlviewer;

import android.content.Context;
import android.widget.Toast;

public class
LoginAuthentication {
    private DataManager dataManager;
    private Context c;
    private static final String PREFERENCE = "Profiles";
    private static final String[] KEYS = {"username", "endpoint", "address"};

    public LoginAuthentication(Context c) {
        dataManager = new DataManager(c, PREFERENCE);
        this.c = c;
    }

    //Authenticates the login/profile
    public boolean authenticate() {
        return false;
    }


    public void createNewProfile(String[] data) {
        dataManager.addSharedPreferences(KEYS, data);
        Toast.makeText(c, "Creating Profile", Toast.LENGTH_SHORT).show();
    }

    public void getProfile() {
        for (int i = 0; i < KEYS.length; i++) {
            dataManager.printSharedPreference(KEYS[i]);
        }
    }

    //Returns profile as array: username, endpoint, password
    public String[] returnProfile() {
        String[] retList = dataManager.getSharedPreferences(KEYS);
        Toast.makeText(c, "Getting Profiles", Toast.LENGTH_SHORT).show();
        return retList;
    }

    public void removeProfiles() {
        dataManager.removeKeys(KEYS);
        Toast.makeText(c, "Removed Profiles", Toast.LENGTH_SHORT).show();
    }

}

