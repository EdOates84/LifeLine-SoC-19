package com.example.lifeline;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends AppCompatActivity {

    Button button;
    EditText name, mail, pass;
    FirebaseAuth fAuth;
    FirebaseUser current_user;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        button = findViewById(R.id.updatebtn);
        name = findViewById(R.id.editname);
        mail = findViewById(R.id.editmail);
//        pass =findViewById(R.id.editconfirmpass);
        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();
        final String current = current_user.getEmail();


        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (current.equals(dataSnapshot1.getValue(User.class).getEmail())) {
                                String NAME = name.getText().toString();
//                        String EMAIL = mail.getText().toString().trim();
//                        if(TextUtils.isEmpty(EMAIL)){
//                            mail.setError("Email is required");
//                            return;
//                        }
                                String data = dataSnapshot1.getKey();
                                mDatabase.child(data).child("name").setValue(NAME);
//                        mDatabase.child(data).child("email").setValue(EMAIL);
                                Toast.makeText(SettingsFragment.this, "Profile is Updated", Toast.LENGTH_SHORT).show();


                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}


