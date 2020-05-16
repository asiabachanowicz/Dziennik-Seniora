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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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

        final EditText sugaredit = findViewById(R.id.editText);
        final EditText tempedit = findViewById(R.id.editText2);
        final EditText Spressureedit = findViewById(R.id.editText10);
        final EditText Dpressureedit = findViewById(R.id.editText3);
        final EditText pulseedit = findViewById(R.id.editText4);
        final TextView logdate = findViewById(R.id.currentTime);

        //constant text
        sugaredit.setText(" mg/dL");
        tempedit.setText(" °C");
        Spressureedit.setText(" mmHg");
        Dpressureedit.setText(" mmHg");
        pulseedit.setText(" /min");

        Button sendDatabutt = findViewById(R.id.sendDatabutt);

        //set current time
        TextView date;
        Date currentTime = Calendar.getInstance().getTime();
        date = (TextView) findViewById(R.id.currentTime);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        date.setText(dateFormatter.format(currentTime));

        sendDatabutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject reportdata = new JSONObject();
                String user_sugar = sugaredit.getText().toString();
                String user_temp = tempedit.getText().toString();
                String user_Spressure = Spressureedit.getText().toString();
                String user_Dpressure = Dpressureedit.getText().toString();
                String user_pulse = pulseedit.getText().toString();
                String user_logDate = logdate.getText().toString();


                if (user_sugar != "" && user_temp != "" && user_Spressure != "" && user_Dpressure != "" && user_pulse != "") {
                    try {
                        String user_login = FirstFragment.user_login;
                        reportdata.put("sugar", user_sugar);
                        reportdata.put("temperature", user_temp);
                        reportdata.put("systolic blood pressure", user_Spressure);
                        reportdata.put("diastolic blood pressure", user_Dpressure);
                        reportdata.put("pulse", user_pulse);
                        reportdata.put("date", user_logDate);
                        reportdata.put("login", user_login);
                        reportdata.put("type", "dailyraport");

                        Log.e("TAG", reportdata.toString());
                        new SendJSONtoServer().execute("http://192.168.0.66:8080/telematyka-serwer/servletdata", reportdata.toString());
                        Toast.makeText(Raport.this, "Zapisano", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Intent intent4 = new Intent(Raport.this, MainMenu.class);
                startActivity(intent4);
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
        switch (item.getItemId()){
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