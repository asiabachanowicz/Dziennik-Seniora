package com.example.dziennikseniora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_menu);
            Button alertbutt = findViewById(R.id.alertbutt);
            Button calendarbutt = findViewById(R.id.calendarbutt);
            Button reportbutt = findViewById(R.id.reportbutt);
            Button contactbutt = findViewById(R.id.contactbutt);


            alertbutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(MainMenu.this, Alert.class);
                    startActivity(intent1);
                }
            });
            calendarbutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(MainMenu.this, CalendarMenu.class);
                    startActivity(intent2);
                }
            });
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

        //menu bar
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item){
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