package com.example.blueberry2.fragment;

import static android.app.Activity.RESULT_OK;
import static com.example.blueberry2.SignupActivity.PICK_FROM_ALBUM;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.blueberry2.Home.MyYearMonthPickerDialog;
import com.example.blueberry2.Home.ShowActivity;
import com.example.blueberry2.Home.WriteActivity;
import com.example.blueberry2.LoginActivity;
import com.example.blueberry2.R;
import com.example.blueberry2.chat.MessageActivity;
import com.example.blueberry2.model.AlbumModel;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference storageReference=storage.getReference();

    private Button btn_write;
    private String uid;
    private String year;
    private String month;
    private String day;
    private String content;
    private String imgUrl;
    private String subtitle;

    private String username;
    private String profileimgurl; //사용자의 프로필 사진 주소
    private String userid;

    private String picker_year;
    private String picker_month;

    private TextView textView_day;
    private TextView textView_subtitle;
    private ImageView feed_imageView;

    private RecyclerView recyclerView_feed;

    private UserModel userModel_org;
    private UserModel userModel_frd;

    private ImageView my_image;
    private TextView my_name;

    private Button btn_YearMonthPicker;

    private String friend_uid;

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            Log.d("YearMonthPickerTest", "year = " + year + ", month = " + monthOfYear + ", day = " + dayOfMonth);
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView_friend = (RecyclerView) view.findViewById(R.id.homefragment_friend_recyclerview);
        RecyclerView recyclerView_feed=(RecyclerView) view.findViewById(R.id.homefragment_feed_recyclerview);

        recyclerView_friend.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView_friend.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recyclerView_friend.setAdapter(new HomeFragmentRecyclerViewAdapter());
        recyclerView_feed.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView_feed.setAdapter(new HomeFragmentRecyclerViewAdapter_feed());

        btn_write = (Button) view.findViewById(R.id.HomeFragment_btn_write);
        btn_YearMonthPicker=(Button)view.findViewById(R.id.btn_year_month_picker);

        my_name=(TextView) view.findViewById(R.id.HomeFragment_my_name);
        my_image=(ImageView)view.findViewById(R.id.HomeFragment_my_image);

        HomeFragment.InfoAdapter infoAdapter=new HomeFragment.InfoAdapter();
        infoAdapter.getInfoAdapter();


        // 작성하는 화면으로 이동 Fragment->Acticity
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(), WriteActivity.class);
                startActivity(intent);

                recyclerView_feed.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
                recyclerView_feed.setAdapter(new HomeFragmentRecyclerViewAdapter_feed());
            }
        });

        btn_YearMonthPicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                btn_YearMonthPicker.setVisibility(View.INVISIBLE);
                MyYearMonthPickerDialog pd = new MyYearMonthPickerDialog();
                pd.setListener(d);
                pd.show(getFragmentManager(), "YearMonthPickerTest");
                pd.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        picker_year=Integer.toString(i);
                        picker_month=Integer.toString(i1);


                        year=picker_year;
                        month=picker_month;

                        btn_YearMonthPicker.setVisibility(View.VISIBLE);
                        btn_YearMonthPicker.setText(picker_year+"년"+picker_month+"월");

                        recyclerView_feed.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
                        recyclerView_feed.setAdapter(new HomeFragmentRecyclerViewAdapter_feed());

                    }
                });

            }
        });


        return view;
    }

    class HomeFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<UserModel> userModels;
        public HomeFragmentRecyclerViewAdapter(){
            userModels=new ArrayList<>();
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userModels.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                        UserModel userModel=snapshot.getValue(UserModel.class);

                        if(userModel.uid.equals(myUid)){
                            continue;
                        }
                        userModels.add(userModel);
                    }
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pro, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            Glide.with(holder.itemView.getContext())
                    .load(userModels.get(position).profileImageUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(((CustomViewHolder)holder).imageView);
            ((CustomViewHolder)holder).textView.setText(userModels.get(position).userName);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MessageActivity.class);
                    intent.putExtra("destinationUid", userModels. get(position).uid);
                    ActivityOptions activityOptions = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                        activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright, R.anim.toleft);
                        startActivity(intent, activityOptions.toBundle());
                    }



                }
            });

        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }
    }
        class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.HomeFragment_item_pro_profileimage);
                textView = (TextView) view.findViewById(R.id.HomeFragment_item_pro_name);
            }
        }

    class HomeFragmentRecyclerViewAdapter_feed extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<AlbumModel> albumModels;

        public HomeFragmentRecyclerViewAdapter_feed(){
            albumModels=new ArrayList<>();
            //final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            uid=FirebaseAuth.getInstance().getCurrentUser().getUid();



            if(year==null || month==null){
                year="2022";
                month="11";
            }

            FirebaseDatabase.getInstance().getReference().child("albums").child(uid).child(year).child(month).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    albumModels.clear();
                    for(DataSnapshot snapshot :dataSnapshot.getChildren()){

                        AlbumModel albumModel=snapshot.getValue(AlbumModel.class);

                        albumModels.add(albumModel);
//
                    }
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);

            return new CustomViewHolder_feed(view);

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

            Glide.with(holder.itemView.getContext())
                    .load(albumModels.get(position).imgUrl)
                    .into(((CustomViewHolder_feed)holder).imageView_img);
            ((CustomViewHolder_feed)holder).textView_day.setText(albumModels.get(position).day+"일");
            ((CustomViewHolder_feed)holder).textView_subtitle.setText(albumModels.get(position).subtitle);

            // 사진 눌렀을 때
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    friend_uid=albumModels.get(position).uid;

                    Intent intent = new Intent(getContext(), ShowActivity.class);
                    intent.putExtra("click_year", albumModels.get(position).year);
                    intent.putExtra("click_month", albumModels.get(position).month);
                    intent.putExtra("click_day", albumModels.get(position).day);

                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return albumModels.size();
        }
    }
    class CustomViewHolder_feed extends RecyclerView.ViewHolder {
        public TextView textView_day;
        public TextView textView_subtitle;
        public ImageView imageView_img;

        public CustomViewHolder_feed(View view) {
            super(view);
            textView_day=(TextView)view.findViewById(R.id.HomeFragment_item_view_days);
            textView_subtitle=(TextView) view.findViewById(R.id.HomeFragment_item_view_subtitle);
            imageView_img=(ImageView) view.findViewById(R.id.HomeFragment_item_view_ImageView);
        }
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

                    my_name.setText(username);


                    StorageReference pathReference=storageReference.child("userImg/"+profileimgurl);

                    pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            //Glide.with(WriteActivity.this).load(task).into(imageView_user);
                            Glide.with(HomeFragment.this)
                                    .load(profileimgurl)
                                    .apply(new RequestOptions().circleCrop())
                                    .into(my_image);

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
