package com.example.dziennikseniora;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class AddAlert extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private int notificationId = 1;
    Switch switch1;
    boolean mRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);

        mRepeat = true;

        // Set Onclick Listener.
        findViewById(R.id.button1).setOnClickListener(this);
        Button backbutton = findViewById(R.id.button2);

        switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(this);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AddAlert.this, MainMenu.class);
                startActivity(intent1);
            }
        });

    }

    @Override
    public void onClick(View view) {

        Calendar calendar = Calendar.getInstance();
        EditText editText = findViewById(R.id.editText);
        TimePicker timePicker = findViewById(R.id.timePicker);
        DatePicker datePicker = findViewById(R.id.datePicker);
        TextView Notificationdate, Notificationtime, Notificationname;

        Notificationdate = (TextView) findViewById(R.id.textView7);
        Notificationtime = (TextView) findViewById(R.id.textView8);
        Notificationname = (TextView) findViewById(R.id.textView9);


        // Set notificationId & text.
        Intent intent = new Intent(AddAlert.this, AlertReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("todo", editText.getText().toString());


        // getBroadcast(context, requestCode, intent, flags)
        PendingIntent alarmIntent = PendingIntent.getBroadcast(AddAlert.this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);


        switch (view.getId()) {
            case R.id.button1:
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                // Create time.
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);
                startTime.set(Calendar.YEAR, year);
                startTime.set(Calendar.MONTH, month);
                startTime.set(Calendar.DAY_OF_MONTH, day);

                long alarmStartTime = startTime.getTimeInMillis();

                // Set alarm.
                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
                Toast.makeText(this, "Ustawiono przypomnienie", Toast.LENGTH_SHORT).show();

                String not_name = editText.getText().toString();
                Notificationname.setText("Powiadomienie: " +not_name);
                Notificationdate.setText("Data powiadomienia: " +day+ "." +month+ "." +year);
                Notificationtime.setText("Godzina powiadomienia: " +hour+ "." +minute);


                // Repeat
                if (mRepeat == true){
                    alarm.setRepeating(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
                    break;
                }


        }

    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean b){
        if(switch1.isChecked()){
            mRepeat = true;
            Toast.makeText(this, "Włączono powtarzanie powiadomienia!", Toast.LENGTH_SHORT).show();
        }
        else
            mRepeat = false;
    }

}

