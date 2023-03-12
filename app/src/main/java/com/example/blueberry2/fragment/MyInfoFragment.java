package com.example.blueberry2.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blueberry2.Home.WriteActivity;
import com.example.blueberry2.LoginActivity;
import com.example.blueberry2.MainActivity;
import com.example.blueberry2.R;
import com.example.blueberry2.model.UserModel;
import com.example.blueberry2.setting.AskActivity;
import com.example.blueberry2.setting.PopupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.zip.Inflater;


public class MyInfoFragment extends Fragment {

    private FirebaseAuth mAuth;
    private String uid;

    private FirebaseDatabase mDatabase;
    private DatabaseReference databaseReference;

    private Button btn_myname;
    private Button btn_logout;
    private Button btn_ask;

    private TextView textview_username;
    private TextView textview_useremail;

    public String profileimgurl;
    private String username;
    private String email;

    private ImageView profile;

    private ValueEventListener valueEventListener;

    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference storageReference=storage.getReference();

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener; // 로그인 됬는지 확인해주는 리스너

    private UserModel userModel_org;


    public void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_my_info, container,false);
    }

    @Override
    public void onResume() {
        super.onResume();

        InfoAdapter infoAdapter=new InfoAdapter();
        infoAdapter.getInfoAdapter();
    }

    @Override
    public void onStart() {
        super.onStart();


        btn_myname = getView().findViewById(R.id.btn_myname); //이름수정 버튼
        btn_ask = getView().findViewById(R.id.btn_ask);
        btn_logout = getView().findViewById(R.id.btn_logout); //로그아웃 버튼


        textview_username=(TextView)getView().findViewById(R.id.myinfo_username);
        textview_useremail=(TextView)getView().findViewById(R.id.myinto_useremail);

        profile=(ImageView)getView().findViewById(R.id.imageButton9);

        InfoAdapter infoAdapter=new InfoAdapter();
        infoAdapter.getInfoAdapter();




        btn_myname.setOnClickListener(new View.OnClickListener() { //이름수정
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PopupActivity.class);
                startActivity(intent);
            }
        });



        btn_ask.setOnClickListener(new View.OnClickListener() { //문의하기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AskActivity.class);
                startActivity(intent);

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() { //로그아웃
            @Override
            public void onClick(View view) {
                signOut();
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    class InfoAdapter {


        public void getInfoAdapter(){

            uid=FirebaseAuth.getInstance().getCurrentUser().getUid(); // 내 uid 가져오기
            email=FirebaseAuth.getInstance().getCurrentUser().getEmail();


            FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    userModel_org=dataSnapshot.getValue(UserModel.class);
                    username=userModel_org.userName;
                    profileimgurl=userModel_org.profileImageUrl;

                    btn_myname.setText(username);

                    StorageReference pathReference=storageReference.child("userImg/"+profileimgurl);
                    pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            //Glide.with(WriteActivity.this).load(task).into(imageView_user);
                            Glide.with(MyInfoFragment.this)
                                    .load(profileimgurl)
                                    .apply(new RequestOptions().circleCrop())
                                    .into(profile);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    private void signOut() { //로그아웃
        FirebaseAuth.getInstance().signOut();

    }



}