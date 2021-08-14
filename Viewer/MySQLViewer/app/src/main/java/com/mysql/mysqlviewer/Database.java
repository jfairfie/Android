package com.mysql.mysqlviewer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Database {
    private Context c;
    private String address;

    public Database(Context c, String address) {
        this.c = c;
        this.address = "https://bptinventory.a2hosted.com/phpscripts/";
    }

    //Authentication for username and password
    public void Login(Map map) {
        try {
            String data = returnData(map);
            String request = address + "dbConnection.php";
            LoginPHP login = new LoginPHP(data, request, map.get("username").toString(), map.get("password").toString(), map.get("endpoint").toString());
            login.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ViewPHP(Context c, Map map, ListView listView, String[] selectList, String php) {
        String data = returnData(map);
        String request = address + php;
//        System.out.println(request);
        PostPHP post = new PostPHP(c, data, request, listView, selectList);
        post.execute();
    }

    //Converts Map into data that POST request can read
    private String returnData(Map map) {
        String data = null;

        try {
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

    //Sends data to php script at address
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

    private class PostPHP extends AsyncTask<Void, Void, String> {
        private ListView listView;
        private String data, request;
        private Context c;
        private ProgressDialog progressDialog;
        private String[] selectList;

        public PostPHP(Context c, String data, String request, ListView listView, String[] selectList) {
            this.c = c;
            this.listView = listView;
            this.data = data;
            this.request = request;
            //Selectlist are the keys for the json
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
//                    System.out.println(result);
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

    //LoginPHP simply logins the user in, and moves to the next activity
    private class LoginPHP extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog progressDialog;
        private String data, request, username, password, endpoint;

        public LoginPHP(String data, String request, String username, String password, String endpoint) {
            this.request = request;
            this.data = data;
            this.username = username;
            this.password = password;
            this.endpoint = endpoint;
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
                HttpURLConnection httpURLConnection = writeConnection(request, data);
                try {
                    String retString = readerConnection(httpURLConnection);
                    retString = retString.trim();
                    if (retString.equals("200")) {
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    return false;
                }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if (result == true) {
                Toast.makeText(c, "Logging in...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(c, DataView.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("address", address);
                intent.putExtra("endpoint", endpoint);
                c.startActivity(intent);
            } else {
                Toast.makeText(c, "Wrong credentials to login", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
