package com.example.dziennikseniora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button databutt = findViewById(R.id.databutt);
        Button calendarbutt = findViewById(R.id.calendarbutt);
        Button reportbutt = findViewById(R.id.reportbutt);
        Button contactbutt = findViewById(R.id.contactbutt);

        /*
        databutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainMenu.this, .class);
                startActivity(intent1);
            }
        });

        calendarbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainMenu.this, .class);
                startActivity(intent2);
            }
        });
        */
        reportbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainMenu.this, Raport.class);
                startActivity(intent3);
            }
        });
        /*
        contactbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(MainMenu.this, .class);
                startActivity(intent4);
            }
        });
        */

    }
}