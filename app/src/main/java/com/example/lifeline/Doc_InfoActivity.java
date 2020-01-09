package com.example.lifeline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Doc_InfoActivity extends AppCompatActivity {
    ImageView Image;
    TextView name, graduate, dpt, doc_info;
    String Doctorname, Doctorgraduate, Doctordpt, Docimage, Docinfo;
    Button button;
    private DatabaseReference mDatabase;
    private DatabaseReference fDatabase;
    SharedPreferenceConfig preferenceConfig;
    FirebaseAuth dAuth;
    FirebaseUser Current_User;
    int tok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_info);

        preferenceConfig = new SharedPreferenceConfig(getApplicationContext());


        doc_info = findViewById(R.id.doc_info);
        Image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        graduate = findViewById(R.id.graduate);
        dpt = findViewById(R.id.dpt);
        Doctorname = getIntent().getStringExtra("name");
        Doctorgraduate = getIntent().getStringExtra("graduate");
        Doctordpt = getIntent().getStringExtra("dpt");
        Docinfo = getIntent().getStringExtra("doc_info");
        Docimage = getIntent().getStringExtra("image");
        name.setText(Doctorname);
        Log.e("jhfklsdajflk;", "dsfnhkdjsh" + Doctorname);
        graduate.setText(Doctorgraduate);
        dpt.setText(Doctordpt);

        Picasso.with(this).load(Docimage).into(Image);
        dAuth = FirebaseAuth.getInstance();
        Current_User = dAuth.getCurrentUser();
        final String current_name = Current_User.getEmail();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors_List");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    String value = dataSnapshot2.child("doc_info").getValue().toString();
                    doc_info.setText(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        button = findViewById(R.id.bookbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                {
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors_List");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.i("Function", "Called");
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (Doctorname.equals(dataSnapshot1.getValue(Doctor.class).getDoc_name())) {
                                    Log.e("doc_name", "ahkfjhkj" + dataSnapshot1.getValue(Doctor.class).getDoc_name());
                                    Log.e("doc_token", "ahkfjhkj" + dataSnapshot1.getValue(Doctor.class).getDoc_token());
                                    String current_name = dataSnapshot1.getValue(Doctor.class).getDoc_name();
                                    tok = dataSnapshot1.getValue(Doctor.class).getDoc_token() + 1;
                                    String key = dataSnapshot1.getKey();

                                    mDatabase.child(key).child("doc_token").setValue(tok);


                                }
                            } //loop ends

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                {
                    fDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                    fDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot3 : dataSnapshot.getChildren()) {
                                if (current_name.equals(dataSnapshot3.getValue(User.class).getEmail())) {
                                    Log.e("name", "name" + dataSnapshot3.getValue(User.class).getName());
                                    Log.e("current", "current" + Current_User);
                                    String data = dataSnapshot3.getKey();
                                    fDatabase.child(data).child("my_doctor").setValue(Doctorname);
                                    fDatabase.child(data).child("my_token").setValue(tok);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
//
                Toast.makeText(Doc_InfoActivity.this, "Successfully Booked!", Toast.LENGTH_SHORT).show();

            }

        });
    }
}

