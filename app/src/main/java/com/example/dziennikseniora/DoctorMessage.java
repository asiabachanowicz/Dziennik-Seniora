package com.example.dziennikseniora;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.TextView;

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

public class DoctorMessage extends AppCompatActivity {
    TextView doctor_msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        String server_url = "http://192.168.0.66:8080/telematyka-serwer/wiadomosc";
        JSONObject logindata = new JSONObject();
        try {
            logindata.put("login", FirstFragment.user_login);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new DoctorMessage.RaportOperation().execute(server_url, logindata.toString());
    }
    private class RaportOperation extends AsyncTask<String, Void, String> {

        private String jsonResponse;


        protected void onPreExecute() {
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
            Log.e("TAG", result);
            String msg = result.substring(29);
            doctor_msg = findViewById(R.id.textView7);
            doctor_msg.setText(msg);
        }
    }
}
