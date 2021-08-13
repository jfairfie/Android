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

    public StructureManager(ListView listView, Map map, Database database) {
        this.listView = listView;
        this.location = tier.DATABASE;
        this.database = database;
        this.map = map;
    }


    public void moveUp(String selectedItem, Context c, ListView listView) {
        //Moveup tier
//        if (location.inUpperBound()) {
//            Pair<String, String> pair = retPHP(selectedItem);
//            String[] selectList = {pair.first};
//            this.database.ViewPHP(c, this.map, this.listView, selectList, pair.second);
//            listView = this.listView;
//            location = location.up();
//        }

        switch(location) {
            case DATABASE: //Moving up from database to tables
                this.location = tier.TABLES;
                this.database.ViewPHP(c, this.map, this.listView, new String[]{"Database"}, "showDatabases.php");
                return;
            case TABLES: //Moving up from tables to columns
                this.location = tier.COLUMNS;
                this.map.put("database", selectedItem);
                this.database.ViewPHP(c, this.map, this.listView, new String[]{"tablename"}, "showTables.php");
                return;
            case COLUMNS:
                this.location = tier.END; //Setting location to beyond last, cannot go any further
                this.map.put("table", selectedItem);
                this.database.ViewPHP(c, this.map, this.listView, new String[]{"columnname"}, "showColumns.php");
                return;
        }
    }

    public void moveDown(Context c, ListView listView) {
        if (this.location == tier.END) {
            this.location = tier.COLUMNS;
        }

        switch (location) {
            case COLUMNS: //Going from columns to displaying tables
                this.location = tier.TABLES;
                this.map.remove("table");
                this.database.ViewPHP(c, this.map, this.listView, new String[]{"tablename"}, "showTables.php");
                return;
            case TABLES: //Going from tables to displaying databases
                this.location = tier.DATABASE;
                this.map.remove("database");
                this.database.ViewPHP(c, this.map, this.listView, new String[]{"Database"}, "showDatabases.php");
                return;
        }
    }


    //Returns php used for returning each tier
    private Pair<String, String> retPHP(String data) {
        switch (location) {
            case DATABASE:
                return new Pair<String, String>("Database","showDatabases.php");
            case TABLES:
                insertMap("database", data);
                return new Pair<String, String>("tablename", "showTables.php");
            case COLUMNS:
                insertMap("table", data);
                return new Pair<String, String>("columnname", "showColumns.php");
            default:
                return null;
        }
    }

    private void insertMap(String key, String data) {
        if (this.map.get(key) == null) {
            this.map.put(key, data); //Moveup
        } else {
            this.map.remove(key); //Movedown
        }
    }

    //Tracks where where the
    private enum tier {
        DATABASE, TABLES, COLUMNS, END; //levels 0, 1, 2...

        private static tier[] vals = values();

        public tier up() {
            if (inUpperBound()) {

                return vals[(this.ordinal()+1) % vals.length];
            }
            return null;
        }

        private boolean inUpperBound() {
            //Returns true if going down or up a value will still be in bounds
//            System.out.println(this.ordinal() + " " + vals.length);
            if (this.ordinal() < vals.length) {
                return true;
            } else {
                return false;
            }
        }
    }
}