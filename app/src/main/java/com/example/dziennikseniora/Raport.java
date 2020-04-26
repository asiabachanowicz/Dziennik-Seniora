package com.example.dziennikseniora;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Raport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raport);

        final EditText weightedit = findViewById(R.id.editText5);
        final EditText heightedit = findViewById(R.id.editText6);
        final EditText sugaredit = findViewById(R.id.editText7);
        final EditText tempedit = findViewById(R.id.editText8);
        final EditText pressureedit = findViewById(R.id.editText9);
        final EditText pulseedit = findViewById(R.id.editText10);
        final TextView logdate = findViewById(R.id.currentTime);

        Button sendDatabutt = findViewById(R.id.sendDatabutt);

        TextView date;
        Date currentTime = Calendar.getInstance().getTime();
        date = (TextView) findViewById(R.id.currentTime);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        date.setText(dateFormatter.format(currentTime));


        final Spinner spinner = (Spinner)findViewById(R.id.spinner1);
        String[] elementy = {"A Rh+", "A Rh-", "B Rh+", "B Rh-", "AB Rh+", "AB Rh-", "0 Rh+", "0 Rh-"};
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, elementy);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int id, long position) {

                //Toast.makeText(Raport.this, "Wybrano opcję" + (id+1), Toast.LENGTH_SHORT).show();

                switch((int)position)
                {
                    case 0:
                        //wybrano pierwszy element
                        break;
                    case 1:
                        //wybrano drugi element
                        break;
                    case 2:
                        //wybrano trzeci element
                        break;
                    case 3:
                        //wybrano czwarty element
                        break;
                    case 4:
                        //wybrano piąty element
                        break;
                    case 6:
                        //wybrano piąty element
                        break;
                    case 7:
                        //wybrano piąty element
                        break;
                    case 8:
                        //wybrano piąty element
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        sendDatabutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject reportdata = new JSONObject();
                String user_weight = weightedit.getText().toString();
                String user_height = heightedit.getText().toString();
                String user_sugar = sugaredit.getText().toString();
                String user_temp = tempedit.getText().toString();
                String user_pressure = pressureedit.getText().toString();
                String user_pulse = pulseedit.getText().toString();
                String user_logDate = logdate.getText().toString();


                if (user_weight != "" && user_height != "" && user_sugar != "" && user_temp != "" && user_pressure != "" && user_pulse != "") {
                    try {
                        reportdata.put("weight", user_weight);
                        reportdata.put("height", user_height);
                        reportdata.put("sugar", user_sugar);
                        reportdata.put("temperature", user_temp);
                        reportdata.put("blood pressure", user_pressure);
                        reportdata.put("pulse", user_pulse);
                        reportdata.put("date", user_logDate);

                        Log.e("TAG", reportdata.toString());
                        new SendJSONtoServer().execute("http://192.168.0.66:8080/telematyka-serwer/servletdata", reportdata.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }



}