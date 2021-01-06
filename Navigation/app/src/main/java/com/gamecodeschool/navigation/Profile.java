package com.gamecodeschool.navigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    ImageView image;
    TextView user_name,user_mail;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    String userID;
    DatabaseReference userRef;
    DatabaseReference checkRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth=FirebaseAuth.getInstance();
        image = (ImageView) findViewById(R.id.mypropic);
        user_name = (TextView) findViewById(R.id.mypro_name);
        user_mail = (TextView) findViewById(R.id.mypro_email);
    }
    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();
        setProfile();
    }

    private void setupProfile() {
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        userRef.child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String img = (String) dataSnapshot.getValue();
                if (!img.equals("Not yet setup"))
                    Picasso.with(getApplicationContext()).load(img).into(image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userRef.child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mail = (String) dataSnapshot.getValue();
                user_mail.setText(mail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.getValue();
                user_name.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setProfile() {
        checkRef=FirebaseDatabase.getInstance().getReference();
        checkRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userID)) {
                    setupProfile();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
