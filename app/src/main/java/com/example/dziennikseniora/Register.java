package com.example.dziennikseniora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText loginedit = findViewById(R.id.editText5);
        final EditText passwordedit = findViewById(R.id.editText6);
        final EditText weightedit = findViewById(R.id.editText);
        final EditText heightedit = findViewById(R.id.editText2);
        final JSONObject registerdata = new JSONObject();

        //constant text
        weightedit.setText(" kg");
        heightedit.setText(" cm");

        Button sendRegisterData = findViewById(R.id.sendRegisterDatabutt);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        //list with blood groups
        List<String> list = new ArrayList<>();
        list.add("A Rh+");
        list.add("A Rh-");
        list.add("B Rh+");
        list.add("B Rh-");
        list.add("AB Rh+");
        list.add("AB Rh-");
        list.add("0 Rh+");
        list.add("0 Rh-");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(Register.this, "Wybrano opcję" + selectedItem, Toast.LENGTH_SHORT).show();
                try {
                    registerdata.put("blood_gr", selectedItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        sendRegisterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_login = loginedit.getText().toString();
                String user_password = passwordedit.getText().toString();
                String user_weight = weightedit.getText().toString();
                String user_height = heightedit.getText().toString();

                if (user_login != "" && user_password != "" && user_weight != "" && user_weight != "") {
                    try {
                        registerdata.put("login", user_login);
                        registerdata.put("password", user_password);
                        registerdata.put("weight", user_weight);
                        registerdata.put("height", user_height);

                        Log.e("TAG", registerdata.toString());
                        new Register.RegisterOperation().execute("http://192.168.0.66:8080/telematyka-serwer/register", registerdata.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(Register.this, MainMenu.class);
                startActivity(intent);
            }
        });

    }

    //menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Edytowanie zdjęcia profilowego", Toast.LENGTH_SHORT).show();
            case R.id.item2:
                Toast.makeText(this, "Edytowanie", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Toast.makeText(this, "Wylogowano", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class RegisterOperation extends AsyncTask<String, Void, String> {

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
        }
    }
}