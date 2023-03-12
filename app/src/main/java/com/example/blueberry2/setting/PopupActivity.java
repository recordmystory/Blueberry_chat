package com.example.blueberry2.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blueberry2.LoginActivity;
import com.example.blueberry2.MainActivity;
import com.example.blueberry2.R;
import com.example.blueberry2.chat.MessageActivity;
import com.example.blueberry2.fragment.MyInfoFragment;
import com.example.blueberry2.model.ChatModel;
import com.example.blueberry2.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class PopupActivity extends FragmentActivity{

    private EditText txtText;

    private Button btn;

    private String uid;
    private String username;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private String profileimgurl;


    private Button button;

    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀바 없애기 -> 이거 없앴더니 PopupActivity 실행돼!


        //UI 객체생성

        InfoAdapter infoAdapter=new InfoAdapter();
        infoAdapter.getInfoAdapter();


        txtText = (EditText) findViewById(R.id.myinfo_popup_txtText);



        button=(Button) findViewById(R.id.myinfo_popup_btn);

        //profileimgurl=userModel_org.profileImageUrl;

        //profileimgurl="ccc";
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid(); // 내 uid 가져오기
        databaseReference= FirebaseDatabase.getInstance().getReference();






        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtText.getText().toString().equals("")){
                    return;
                }

                UserModel userModel=new UserModel();
                userModel.uid=uid;
                userModel.profileImageUrl=profileimgurl;
                userModel.userName=txtText.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Intent intent = new Intent(PopupActivity.this, MyInfoFragment.); //인텐트를 통해 Main 액티비티 호출
                        //startActivity(intent);
                        //FragmentTransaction transaction=getFragmentManager().beginTransaction();
                        //transaction.replace(R.id.mainactivity_framelayout, new MyInfoFragment()).commit();
                        //MyInfoFragment fragment1=new MyInfoFragment();
                        //getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new MyInfoFragment()).commit();
                        finish();


                    }
                });



            }
        });

    }

    class InfoAdapter {
        UserModel userModel_org;


        public void getInfoAdapter(){
            uid=FirebaseAuth.getInstance().getCurrentUser().getUid(); // 내 uid 가져오기

            FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userModel_org=dataSnapshot.getValue(UserModel.class);
                    username=userModel_org.userName;
                    profileimgurl=userModel_org.profileImageUrl;
                    userid=userModel_org.uid;


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
//    public class goFragment extends FragmentActivity{
//        public void goMyInfo(){
//            MyInfoFragment fragment1=new MyInfoFragment();
//            getSupportFragmentManager().beginTransaction().replace(R.id.myinfo_popup_btn, new MyInfoFragment()).commit();
//
//        }
//    }


}