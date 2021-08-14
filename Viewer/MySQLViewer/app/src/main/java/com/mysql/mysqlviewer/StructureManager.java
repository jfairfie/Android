package com.mysql.mysqlviewer;

import android.content.Context;
import android.util.Pair;
import android.widget.ListView;

import java.util.Map;

public class StructureManager {
    private ListView listView;
    private String username, password, endpoint;
    private tier location;
    private Database database;
    private Map map;
    private Context c;

    public StructureManager(Context c, ListView listView, Map map, Database database) {
        this.listView = listView;
        this.location = tier.DATABASE;
        this.database = database;
        this.map = map;
        this.c = c;
        onCreate();
    }

    private void onCreate() {
        this.database.ViewPHP(c, this.map, this.listView, new String[]{"Database"}, "showDatabases.php");
    }


    // - DATABASE   -
    // -- Displays Databases --
    public void moveUp(String selectedItem) {
        switch (location) {
            case DATABASE: //Displays next tier, tables
                this.location = tier.TABLES;
                this.map.put("database", selectedItem);
                this.database.ViewPHP(this.c, this.map, this.listView, new String[]{"tablename"}, "showTables.php");
                return;
            case TABLES: //Displays columns
                this.location = tier.COLUMNS;
                this.map.put("table", selectedItem);
                this.database.ViewPHP(this.c, this.map, this.listView, new String[]{"columnname"}, "showColumns.php");
                return;
        }
    }
    public void moveDown() {
        System.out.println(this.map);
        switch (location) {
            case COLUMNS:
                this.map.remove("table");
                this.location = tier.TABLES;
                this.database.ViewPHP(c, map, listView, new String[]{"tablename"}, "showTables.php");
                return;
            case TABLES:
                this.location = tier.DATABASE;
                this.map.remove("database");
                this.database.ViewPHP(c, this.map, this.listView, new String[]{"Database"}, "showDatabases.php");
                return;
        }
    }

    //Tracks where where the
    private enum tier {
        DATABASE, TABLES, COLUMNS, END; //levels 0, 1, 2...

    }

}