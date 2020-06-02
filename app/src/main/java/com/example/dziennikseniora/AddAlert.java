package com.example.dziennikseniora;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class AddAlert extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private int notificationId = 1;
    Switch switch1;
    boolean mRepeat;
    public long timeRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);

        // Set Onclick Listener.
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);

        switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {

        Calendar calendar = Calendar.getInstance();

        mRepeat = true;
        EditText editText = findViewById(R.id.editText);
        TimePicker timePicker = findViewById(R.id.timePicker);
        DatePicker datePicker = findViewById(R.id.datePicker);

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
                // set(type, milliseconds, intent)
                alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
                // Repeat

                alarm.setRepeating(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

                Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.button2:
                alarm.cancel(alarmIntent);
                Toast.makeText(this, "Canceled.", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean b){
        if(switch1.isChecked()){
            mRepeat = true;
            Toast.makeText(AddAlert.this, "Włączono powtarzanie powiadomienia", Toast.LENGTH_SHORT).show();
        }
        else
            mRepeat = false;
        Toast.makeText(AddAlert.this, "Wyłączono powtarzanie powiadomienia", Toast.LENGTH_SHORT).show();
    }

}
