package com.mysql.mysqlviewer;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.StringPrepParseException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//Manages data stored locally
public class DataManager {
    private Context c;
    private String preference;
    private SharedPreferences sharedPreferences;

    public DataManager(Context c, String preference) {
        this.preference = preference;
        this.c = c;
        getPreference();
    }

    //Simply instantiates SharedPreferences
    private void getPreference() {
        sharedPreferences = c.getSharedPreferences(preference, Context.MODE_PRIVATE);
    }

    //Adds one item to the SharedPreference
    public void addSharedPreference(String key, String data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, data);
        editor.commit();
    }

    //Adds an array of items to the shared preference
    public void addSharedPreferences(String keys[], String[] data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < keys.length; i++) {
            editor.putString(keys[i], data[i]);
        }
        editor.commit();
    }

    //Returns data as a string from key
    public String getSharedPreference(String key) {
        return sharedPreferences.getString(key, "No key is present");
    }

    //Prints data from key
    public void printSharedPreference(String key) {
        System.out.println(sharedPreferences.getString(key, "No key is present"));
    }

    //Returns a list of data from keys
    public String[] getSharedPreferences(String[] keys) {
        String[] retList = new String[keys.length];
        for (int i = 0; i < keys.length; i++) {
            retList[i] = sharedPreferences.getString(keys[i], "No key is present");
        }
        return retList;
    }

    //Removes one key
    public void removeKey(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
    }

    //Removes multiple keys
    public void removeKeys(String[] keys) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < keys.length; i++) {
            editor.remove(keys[i]);
            editor.apply();
        }
    }
}