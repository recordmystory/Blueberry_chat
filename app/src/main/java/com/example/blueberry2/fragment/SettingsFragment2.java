package com.example.blueberry2.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.blueberry2.setting.MyInfoActivity;

public class SettingsFragment2 extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getActivity(), MyInfoActivity.class);
        startActivity(intent);
    }
}
