package com.gamecodeschool.navigation;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import static com.gamecodeschool.navigation.R.layout.drawermenu;

public class Drawermenu extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawermenu);
        firebaseAuth=FirebaseAuth.getInstance();
        final TextView registerLink = (TextView) findViewById(R.id.logout);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(Drawermenu.this, LoginActivity.class));
            }
        });


    }


}
