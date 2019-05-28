package com.example.semesterprojectv_0_7;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class reminderActivity extends AppCompatActivity {

    private gpsFragment GPSFragment = new gpsFragment();
    private reminderFragment RemFragment = new reminderFragment();
    private Data data;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(reminderActivity.this,MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_date:

                    return true;
                case R.id.navigation_gps:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,GPSFragment).commit();
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,RemFragment).commit();

        BottomNavigationView navView = findViewById(R.id.secondBtView);



        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


}
