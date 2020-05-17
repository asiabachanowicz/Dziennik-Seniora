package com.example.dziennikseniora;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calen);
        CalendarView CV = findViewById(R.id.calendarView);
        CV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // make 2020-05-17 12:31:57
                String choosen_date;
                if (month == 10 || month == 11) {
                     choosen_date = year + "-" + (month+1) + "-" + dayOfMonth;
                    Log.e("TAG", choosen_date);
                } else {
                    choosen_date = year + "-0" + (month+1) + "-" + dayOfMonth;
                    Log.e("TAG", choosen_date);
                }
                String server_url = "http://192.168.1.24:8080/telematyka-serwer/raport";
                new RaportOperation().execute(server_url, choosen_date);
            }
        });
    }
        private class RaportOperation extends AsyncTask<String, Void, String> {

            private String jsonResponse;
            private ProgressDialog dialog = new ProgressDialog(CalendarMenu.this);
            protected void onPreExecute() {
                dialog.setMessage("Please wait..");
                dialog.show();
            }
            @Override
            protected String doInBackground(String... params) {
                String data = "";

                HttpURLConnection httpURLConnection = null;
                try {

                    httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                    httpURLConnection.setRequestMethod("POST");

                    httpURLConnection.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                    wr.writeBytes("PostData=" + params[1]);
                    wr.flush();
                    wr.close();

                    InputStream in = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(in);

                    int inputStreamData = inputStreamReader.read();
                    while (inputStreamData != -1) {
                        char current = (char) inputStreamData;
                        inputStreamData = inputStreamReader.read();
                        data += current;
                    }
                    receivedata(httpURLConnection);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }

                return data;
            }

            private void receivedata(HttpURLConnection connection) throws Exception {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String returnString = "";
                StringBuilder allData = new StringBuilder("");

                while ((returnString = in.readLine()) != null) {
                    allData.append(returnString);
                }
                in.close();

                Log.e("TAG", allData.toString());
            }
            protected void onPostExecute(String result) {
                dialog.dismiss();
                Log.e("TAG", result);
                result = result.replace("=", ":");
//                Pattern pattern = Pattern.compile("([}])");
//                Matcher matcher = pattern.matcher(result);
//                int count = 0;
//                while (matcher.find()) count++;
//                Log.e("TAG", "ilosc"+count);
                HashMap<String, String> myHashMap = new HashMap<String, String>();
                ArrayList list = new ArrayList();
                // result example [{"tetno":"10","cisnienie":"5"},{"tetno":"3","cisnienie":"3"}]
                try {
                    JSONArray jArray = new JSONArray(result);
                    JSONObject jObject = null;
                    String ratekey=null;
                    String pulsekey = null;
                    for (int i = 0; i < jArray.length(); i++) {
                        jObject = jArray.getJSONObject(i);
                        // beacuse you have only one key-value pair in each object so I have used index 0
                        ratekey = (String)jObject.names().get(0);
                        myHashMap.put(ratekey, jObject.getString(ratekey));
                        pulsekey = (String)jObject.names().get(1);
                        myHashMap.put(pulsekey, jObject.getString(pulsekey));
                        list.add(myHashMap);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
}


