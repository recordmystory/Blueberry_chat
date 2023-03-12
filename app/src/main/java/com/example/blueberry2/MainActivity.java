package com.example.blueberry2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.blueberry2.fragment.CalendarFragment;
import com.example.blueberry2.fragment.ChatFragment;
import com.example.blueberry2.fragment.HomeFragment;
import com.example.blueberry2.fragment.MyInfoFragment;
import com.example.blueberry2.fragment.PeopleFragment;
import com.example.blueberry2.fragment.SettingsFragment2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new HomeFragment()).commit();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.mainactivity_bottomnavigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_home: //임시로 홈
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new HomeFragment()).commit();
                        return true;
                    case R.id.action_textsms: //우리가 사용할 메세지 목록
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new ChatFragment()).commit();
                        return true;
                    case R.id.action_calendar:    // 캘린더
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new CalendarFragment()).commit();
                        return true;
                    case R.id.action_settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new MyInfoFragment()).commit();
                        return true;


                }
                return false;
            }
        });
    }
}