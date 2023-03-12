package com.example.blueberry2.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.blueberry2.Home.WriteActivity;
import com.example.blueberry2.R;

public class AskActivity extends AppCompatActivity {
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        btn_back=(ImageButton)findViewById(R.id.askactivity_btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AskActivity.super.onBackPressed();
            }
        });
    }
}