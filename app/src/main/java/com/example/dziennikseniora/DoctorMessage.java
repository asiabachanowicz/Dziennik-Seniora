package com.example.dziennikseniora;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DoctorMessage extends AppCompatActivity {
    TextView doctor_msg;
    EditText send_msg;
    Button send_btn;
    String strValue;
    static final JSONObject logindata = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //final String server_url = "http://192.168.1.24:8080/telematyka-serwer/wiadomosc";
        final String server_url = "http://192.168.0.66:8080/telematyka-serwer/wiadomosc";
        final EditText simpleEditText = (EditText) findViewById(R.id.editText7);
        //simpleEditText.getText().clear();
        final TextView logdate = findViewById(R.id.timeView);
        TextView date;
        Date currentTime = Calendar.getInstance().getTime();
        date = (TextView) findViewById(R.id.timeView);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        date.setText(dateFormatter.format(currentTime));
        try {
            logindata.put("login", FirstFragment.user_login);
            logindata.put("tresc", null);
            logindata.put("date", logdate.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new DoctorMessage.RaportOperation().execute(server_url, logindata.toString());
        send_btn = findViewById(R.id.button3);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strValue = simpleEditText.getText().toString();
                String user_logDate = logdate.getText().toString();
                try {
                    logindata.put("tresc", strValue);
                    logindata.put("date", user_logDate);
                    new DoctorMessage.RaportOperation().execute(server_url, logindata.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new DoctorMessage.RaportOperation().execute(server_url, logindata.toString());
            }
        });
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
                Log.e("TAG", msg);
                doctor_msg.setText("Wiadomosc " + msg.substring(0,msg.indexOf("D")) + "\n\n");
                if (!msg.contains("null")) {
                JSONArray jArray = null;
                String dane_leki = msg.substring(msg.indexOf(":")+1);
                try {
                    jArray = new JSONArray(dane_leki.replace("=", ":"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jArray != null) {
                    doctor_msg.append("Leki na dziś: \n\n");
                    try {
                        JSONObject jObject = null;
                        String nazwa_leku = null;
                        String razy_dziennie = null;
                        for (int i = 0; i < jArray.length(); i++) {
                            ArrayList<String> rap = new ArrayList<String>();
                            jObject = jArray.getJSONObject(i);
                            nazwa_leku = (String) jObject.get("nazwa");
                            doctor_msg.append("Nazwa " + nazwa_leku + "\n");
                            razy_dziennie = (String) jObject.get("godzina");
                            doctor_msg.append(razy_dziennie + " razy dziennie\n\n");
                        }
                    } catch (JSONException e) {

                    }
                }
            }
            DoctorMessage.logindata.remove("tresc");
        }
    }
}
