package com.example.blueberry2.Home;

import static com.example.blueberry2.SignupActivity.PICK_FROM_ALBUM;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blueberry2.LoginActivity;
import com.example.blueberry2.R;
import com.example.blueberry2.SignupActivity;
import com.example.blueberry2.fragment.HomeFragment;
import com.example.blueberry2.model.AlbumModel;
import com.example.blueberry2.model.UserModel;
import com.example.blueberry2.setting.PopupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class WriteActivity extends AppCompatActivity {
    private ImageButton btn_back;
    private TextView textView_username;
    private ImageView imageView_user;

    private List<UserModel> userModels;
    private UserModel userModel_org;
    private String uid;

    private String username;
    private String profileimgurl; //사용자의 프로필 사진 주소
    private String userid;


    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference storageReference=storage.getReference();

    private EditText year;
    private EditText month;
    private EditText day;
    private EditText content;
    private String imgurl;
    private EditText subtitle;
    private ImageView uploadimg; //업로드하는 이미지
    private Button btn_uplaoad;
    private Uri imageUri;
    private DatePicker picker;
    private TextView pickeer_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);



        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        btn_back=(ImageButton) findViewById(R.id.writeactivity_btn_back);
        textView_username=(TextView) findViewById(R.id.writeactivity_textview_username);
        imageView_user=(ImageView)findViewById(R.id.imageview_user);

        WriteActivity.InfoAdapter infoAdapter=new WriteActivity.InfoAdapter();
        infoAdapter.getInfoAdapter();

        uploadimg=(ImageView)findViewById(R.id.writeactivity_imageview_photo);
        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });


        content=(EditText)findViewById(R.id.writeactivity_content);
        btn_uplaoad=(Button)findViewById(R.id.writeactivity_btn_upload);
        subtitle=(EditText)findViewById(R.id.writeactivity_subtitle);

        picker=(DatePicker)findViewById(R.id.datePicker1);


        btn_uplaoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subtitle.getText().toString()==null||content.getText().toString()==null||imageUri==null){
                    Toast.makeText(WriteActivity.this, "입력 정보를 확인하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseStorage.getInstance().getReference().child("albumImg").child(uid).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        FirebaseStorage.getInstance().getReference().child("albumImg").child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                AlbumModel albumModel=new AlbumModel();

                                albumModel.year=Integer.toString(picker.getYear());

                                albumModel.month=Integer.toString(picker.getMonth()+1);

                                albumModel.day=Integer.toString(picker.getDayOfMonth());
                                albumModel.content=content.getText().toString();
                                albumModel.imgUrl=imageUrl;
                                albumModel.subtitle=subtitle.getText().toString();
                                albumModel.uid=uid;



                                FirebaseDatabase.getInstance().getReference().child("albums").child(uid)
                                        .child(albumModel.year)
                                        .child(albumModel.month)
                                        .child(albumModel.day)
                                        .setValue(albumModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        finish();

                                    }
                                });

                            }
                        });
                    }
                });
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteActivity.super.onBackPressed();
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

                    textView_username.setText(username);


                    StorageReference pathReference=storageReference.child("userImg/"+profileimgurl);
                    pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            //Glide.with(WriteActivity.this).load(task).into(imageView_user);
                            Glide.with(WriteActivity.this)
                                    .load(profileimgurl)
                                    .apply(new RequestOptions().circleCrop())
                                    .into(imageView_user);

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            uploadimg.setImageURI(data.getData()); //가운데 뷰를 바꿈
            imageUri = data.getData(); //이미지 경로 원본
        }
    }
}