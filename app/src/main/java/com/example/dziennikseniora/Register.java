package com.example.dziennikseniora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                Toast.makeText(Register.this, "Wybrano opcję" +selectedItem , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        sendRegisterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject registerdata = new JSONObject();
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
                        new SendJSONtoServer().execute("http://192.168.0.66:8080/telematyka-serwer/servletdata", registerdata.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
}