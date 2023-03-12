package com.example.blueberry2.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blueberry2.R;
import com.example.blueberry2.model.AlbumModel;
import com.example.blueberry2.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ShowActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private TextView textview_myname;
    private ImageView imageview_my;

    private List<UserModel> userModels;
    private UserModel userModel_org;
    private AlbumModel albumModel;
    private String uid;

    private String username;
    private String profileimgurl; //사용자의 프로필 사진 주소
    private String userid;



    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference storageReference=storage.getReference();

    private String year;
    private String month;
    private String day;
    private TextView year_month_day;
    private TextView textview_content;
    private String content;
    private String imgurl;
    private String subtitle;

    private Uri imageUri;


    private ImageView imageView_myphoto;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        ShowActivity.InfoAdapter infoAdapter=new ShowActivity.InfoAdapter();
        infoAdapter.getInfoAdapter();

        textview_myname=(TextView) findViewById(R.id.textView_myname);
        imageview_my=(ImageView)findViewById(R.id.imageview_mypro);
        year_month_day=(TextView) findViewById(R.id.textview_y_m_d);
        textview_content=(TextView) findViewById(R.id.textview_content);
        imageView_myphoto=(ImageView)findViewById(R.id.imageview_photo);
        btn_back=(ImageButton)findViewById(R.id.imagebackButton);
        /* SecondActivity.java */


        ShowActivity.InfoAdapter infoAdapter1=new ShowActivity.InfoAdapter();
        infoAdapter1.getContentAdapter();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    class InfoAdapter {

        public void getInfoAdapter(){
            uid=FirebaseAuth.getInstance().getCurrentUser().getUid(); // 내 uid 가져오기

            FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userModel_org=dataSnapshot.getValue(UserModel.class);
                    username=userModel_org.userName;
                    profileimgurl=userModel_org.profileImageUrl;
                    userid=userModel_org.uid;

                    textview_myname.setText(username);


                    StorageReference pathReference=storageReference.child("userImg/"+profileimgurl);
                    pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            //Glide.with(WriteActivity.this).load(task).into(imageView_user);
                            Glide.with(ShowActivity.this)
                                    .load(profileimgurl)
                                    .apply(new RequestOptions().circleCrop())
                                    .into(imageview_my);

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        public void getContentAdapter(){

            Intent secondIntent = getIntent();
            year=secondIntent.getStringExtra("click_year");
            month=secondIntent.getStringExtra("click_month");
            day=secondIntent.getStringExtra("click_day");

            uid=FirebaseAuth.getInstance().getCurrentUser().getUid(); // 내 uid 가져오기

            FirebaseDatabase.getInstance().getReference().child("albums").child(uid)
                    .child(year).child(month).child(day).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    albumModel=dataSnapshot.getValue(AlbumModel.class);
                    year=albumModel.year;
                    month=albumModel.month;
                    day=albumModel.day;
                    subtitle=albumModel.subtitle;
                    content=albumModel.content;
                    imgurl=albumModel.imgUrl;

                    textview_myname.setText(username);
                    year_month_day.setText(year+"년 "+month+"월 "+day+"일");
                    textview_content.setText(content);



                    StorageReference pathReference=storageReference.child("albumImg/"+imgurl);
                    pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            //Glide.with(WriteActivity.this).load(task).into(imageView_user);
                            Glide.with(ShowActivity.this)
                                    .load(imgurl)
                                    .apply(new RequestOptions())
                                    .into(imageView_myphoto);

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}
