package com.mysql.mysqlviewer;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//Class manages data stored locally for the apps use, ie. config files
public class DataManager {
    private Context c;

    public DataManager(Context c) {
        this.c = c;
    }

    public void writeFile(String fileName, String data) {
        System.out.println("Writing to file");
        try {
            FileOutputStream fileOutputStream = c.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found Error");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO error");
            e.printStackTrace();
        }
    }

    public String[] returnFiles() {
        return c.fileList();
    }
}