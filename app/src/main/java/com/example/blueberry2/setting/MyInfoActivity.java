package com.example.blueberry2.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.blueberry2.R;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.blueberry2.LoginActivity;
import com.example.blueberry2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String uid;

    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseReference;

    private Button btn_myname;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        btn_myname = findViewById(R.id.btn_myname); //이름수정 버튼
        btn_logout = findViewById(R.id.btn_logout); //로그아웃 버튼
        //Button btn_revoke = findViewById(R.id.btn_revoke); //회원탈퇴 버튼

        btn_myname.setOnClickListener(new View.OnClickListener() { //이름수정
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PopupActivity.class);
                intent.putExtra("data", "Test Popup");
                startActivityForResult(intent, 1);
                //startActivity(intent);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() { //로그아웃
            @Override
            public void onClick(View view) {
                signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

//        btn_revoke.setOnClickListener(new View.OnClickListener() { //회원탈퇴
//            @Override
//            public void onClick(View view) {
//               revokeAccess();
//                //mAuth.getCurrentUser().delete();
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
//            }
//        });

        mAuth = FirebaseAuth.getInstance();

    }
    private void signOut() { //로그아웃
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // 데이터 받기
                String result = data.getStringExtra("result");
                //txtResult.setText(result);
            }
        }
    }



//    private void revokeAccess() { //회원탈퇴


  //      uid=FirebaseAuth.getInstance().getCurrentUser().getUid(); // 내 uid 가져오기

//        mDatabase=FirebaseDatabase.getInstance();
//        databaseReference=mDatabase.getReference().child("users").child(uid);

//        databaseReference.removeValue();


//    }

}