package com.inventory.inv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

//Class Connects to the database, and performs a function (Inserter, Deleter, ViewAll...)
public class Database {
    private String address = ""; //Set address to the apache2, or equivalent server
    private String username, password;
    private Context c;

    public Database(Context c, String username, String password) {
        this.c = c;
        this.username = username;
        this.password = password;
    }

    //Create a new user
    public void AddUser(Map map) {
        String data = returnData(map);
        String request = address + "createUser.php";
        InsertPHP connection = new InsertPHP(c, data, request);
        connection.execute();
    }

    //Inserts new Item
    public void Insert(Map map) {
        String data = returnData(map);
        String request = address + "insertItem.php";
        InsertPHP connection = new InsertPHP(c, data, request);
        connection.execute();
    }

    //Inserts new Entry
    public void InsertEntry(Map map) {
        String data = returnData(map);
        String request = address + "createNewEntry.php";
        InsertPHP connection = new InsertPHP(c, data, request);
        connection.execute();
    }

    //Inserts new category
    public void InsertCategory(Map map) {
        String data = returnData(map);
        String request = address + "createNewCategory.php";
        InsertPHP connection = new InsertPHP(c, data, request);
        connection.execute();
    }

    //Removes Category
    public void RemoveCategory(Map map) {
        String data = returnData(map);
        String request = address + "removeCategory.php";
        InsertPHP connection = new InsertPHP(c, data, request);
        connection.execute();
    }

    //Grants user permissions
    public void GrantUserPermission(Map map) {
        String data = returnData(map);
        String request = address + "grantUserPermissions.php";
        InsertPHP connection = new InsertPHP(c, data, request);
        connection.execute();
    }

    //Removes User Permissions
    public void RevokeUserPermission(Map map) {
        String data = returnData(map);
        String request = address + "revokeUserPermissions.php";
        InsertPHP connection = new InsertPHP(c, data, request);
        connection.execute();
    }

    //GetAll returns all data in SELECT * SQL statement
    public void GetAll(ListView listView, String tableName, String[] selectList) {
        Map map = new HashMap<String, String>();
        map.put("category", tableName);
        String data = returnData(map);
        String request = address + "selectAll.php";

        ViewAllPHP connection = new ViewAllPHP(c, data, request, listView, selectList);
        connection.execute();
    }

    //Returns column names as String in json format
    public String GetColumnNames(String category) {
        Map map = new HashMap<String, String>();
        map.put("category", category);
        String data = returnData(map);
        String request = address + "returnColumnNames.php";
        String str_result = null;
        try {
            str_result = new ColumnNamePHP(c, data, request).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return str_result;
    }

    //Returns json of permissions for a user
    public String GetPermissionNames(String user) {
        Map map = new HashMap<String, String>();
        map.put("user", user);
        String data = returnData(map);
        String request = address + "returnPermissions.php";
        String retString = null;

        try {
            retString = new ColumnNamePHP(c, data, request).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        return retString;
    }

    //Returns json of usernames [{'username',[result]}]
    public String GetUserNames() {
        Map map = new HashMap<String, String>();
        String data = returnData(map);
        String request = address + "returnUserNames.php";
        String retString = null;

        try {
            retString = new ColumnNamePHP(c, data, request).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return retString;
    }

    public String ReturnCategoryNames() {
        Map map = new HashMap<String, String>();
        String data = returnData(map);
        String request = address + "returnTableNames.php";
        String str_result = null;

        try {
            str_result = new ColumnNamePHP(c, data, request).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return str_result;
    }

    //Returns data based on user input
    public void GetBy(ListView listView, Map map, String[] selectList) {
        String data = returnData(map);
        String request = address + "getBy.php";
        ViewAllPHP connection = new ViewAllPHP(c, data, request, listView, selectList);
        connection.execute();
    }

    //Login to the MySQL database
    public void Login() {
        String data = returnData(null);
        String request = address + "dbConnection.php";
        LoginPHP login = new LoginPHP(data, request);
        login.execute();
    }

    //Returns encoded data from map
    private String returnData(Map map) {
        String data = null;

        try {
            data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                    + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            if (map != null) {
                Set set = map.entrySet();
                Iterator itr = set.iterator();
                while (itr.hasNext()) {
                    Map.Entry entry = (Map.Entry) itr.next();
                    data += "&" + URLEncoder.encode(entry.getKey().toString(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue().toString(), "UTF-8");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    //Returns HTTPURLConnection connection from request (url to php), and data (the data to PUSH to php script)
    private HttpURLConnection writeConnection(String request, String data) {
        HttpURLConnection connection = null;
        OutputStream os = null;
        BufferedWriter bufferedWriter = null;

        try {
            URL url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            os = connection.getOutputStream();
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bufferedWriter.write(data);
            bufferedWriter.flush();
        } catch (MalformedURLException urlException) {
            urlException.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Checking that everything went right and the stream is now closed
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return connection;
    }

    //Returns String from HTTPUrlConnection (PHP echos)
    private String readerConnection(HttpURLConnection connection) {
        InputStream inputStream = null;
        StringBuffer buffer = null;

        try {
            String line = "";
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            buffer = new StringBuffer();

            if (bufferedReader != null) {
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Closes input stream at end of reader
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return buffer.toString();
    }

    //Delete PHP deletes an item from a table
    private class DeletePHP extends AsyncTask<Void, Void, Void> {
        private Context c;
        private String data, request;
        private ProgressDialog progressDialog;

        public DeletePHP(Context c, String data, String request) {
            this.c = c;
            this.data = data;
            this.request = request;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(c);
            progressDialog.setTitle("Deleting item");
            progressDialog.setMessage("Deleting...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpURLConnection httpURLConnection = writeConnection(request, data);
            String retString = readerConnection(httpURLConnection);
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.setMessage("Done..");
            progressDialog.dismiss();
        }
    }

    //ViewAllPHP displays SELECT * to ListView
    private class ColumnNamePHP extends AsyncTask<Void, Void, String> {
        private String data, request;
        private Context c;
        private ProgressDialog progressDialog;

        public ColumnNamePHP(Context c, String data, String request) {
            this.c = c;
            this.data = data;
            this.request = request;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(c);
            progressDialog.setTitle("Getting Names");
            progressDialog.setMessage("Getting Names...");
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection httpURLConnection = writeConnection(request, data);
            String retString = readerConnection(httpURLConnection);
            return retString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                    }
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }
    }

    //ViewAllPHP displays SELECT * to ListView
    private class ViewAllPHP extends AsyncTask<Void, Void, String> {
        private ListView listView;
        private String data, request;
        private Context c;
        private ProgressDialog progressDialog;
        private String[] selectList;

        public ViewAllPHP(Context c, String data, String request, ListView listView, String[] selectList) {
            this.c = c;
            this.listView = listView;
            this.data = data;
            this.request = request;
            this.selectList = selectList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(c);
            progressDialog.setTitle("Viewing Items");
            progressDialog.setMessage("Finding Items...");
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection httpURLConnection = writeConnection(request, data);
            String retString = readerConnection(httpURLConnection);
            return retString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    ArrayList<String> items = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        String jsonString = "";
                        for (int j = 0; j < selectList.length; j++) {
                            jsonString += jsonObject.getString(selectList[j]).trim() + " ";
                        }
                        items.add(jsonString);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, items);
                    listView.setAdapter(adapter);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }
    }

    //InsertPHP uses inserts data into the mysql database
    private class InsertPHP extends AsyncTask<Void, Void, Boolean> {
        private String data, request;
        ProgressDialog progressDialog;

        public InsertPHP(Context c, String data, String request) {
            this.data = data;
            this.request = request;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(c);
            progressDialog.setTitle("Inserting Data");
            progressDialog.setMessage("Inserting...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpURLConnection httpURLConnection = null;
            try {
                //Establishing connection to url
                URL url = new URL(request);
                httpURLConnection = writeConnection(request, data);

                //Reading echo from php script
                String retString = readerConnection(httpURLConnection);

            } catch (MalformedURLException urlException) {
                urlException.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Toast.makeText(c, "Successfully inserted!", Toast.LENGTH_SHORT).show();
        }
    }

    //LoginPHP simply logins the user in, and moves to the next activity
    private class LoginPHP extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog progressDialog; 
        private String data, request;

        public LoginPHP(String data, String request) {
            this.request = request;
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(c); 
            progressDialog.setTitle("Login"); 
            progressDialog.setMessage("Logging in");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                HttpURLConnection httpURLConnection = writeConnection(request, data);
                String output = "";
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    String line;
                    while ((line = in.readLine()) != null) {
                        //The connection was successful, hence the username and password was valid
                        output += line;
                    }
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }

                if (output.trim().equals("Connection") == true) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result == true) {
                Toast.makeText(c, "Logging in...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(c, SelectionActivity.class);
                intent.putExtra("Username", username);
                intent.putExtra("Password", password);
                c.startActivity(intent);
            } else {
                Toast.makeText(c, "Wrong credentials to login", Toast.LENGTH_SHORT).show();
            }
        }
    }
}