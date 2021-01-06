package com.gamecodeschool.navigation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PostSingleActivity extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference mDatabase, likeDatabase;
    private FirebaseAuth mAuth;
    private TextView mSinglePostTitle;
    private ImageView mSinglePostImage;
    private TextView mSinglePostSeat;
    private TextView mSinglePostRentPerHour;
    private TextView mSinglePostRentPerDay;
    private TextView mSinglePostRentPerDayOutside;
    private Spinner spinner;
    private Button phn;
    private Button delete;
    private boolean call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_single);
        mSinglePostTitle = (TextView) findViewById(R.id.SinglePostTitle);
        mSinglePostImage = (ImageView) findViewById(R.id.SinglePostImage);
        mSinglePostSeat = (TextView) findViewById(R.id.SinglePostSeat);
        mSinglePostRentPerHour = (TextView) findViewById(R.id.perHourCost);
        mSinglePostRentPerDay = (TextView) findViewById(R.id.perDayCost);
        mSinglePostRentPerDayOutside = (TextView) findViewById(R.id.perDayOutsideCost);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mPost_key = getIntent().getExtras().getString("post_id");
        spinner = findViewById(R.id.place_spn);
        mAuth = FirebaseAuth.getInstance();

        phn = findViewById(R.id.contactbtn);
        delete = findViewById(R.id.deletebtn);

        phn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call=true;
                mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(call)
                        {
                            String mobile = dataSnapshot.child("Contact_No").getValue().toString();
                            Toast.makeText(PostSingleActivity.this, ""+mobile, Toast.LENGTH_SHORT).show();
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + mobile));
                            if (ActivityCompat.checkSelfPermission(PostSingleActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            call=false;
                            startActivity(callIntent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_uid = (String) dataSnapshot.child("userId").getValue();
                if(!mAuth.getCurrentUser().getUid().equals(post_uid))
                {
                    //Toast.makeText(PostSingleActivity.this, mAuth.getCurrentUser().getUid()+" "+post_uid, Toast.LENGTH_SHORT).show();
                   delete.setVisibility(View.GONE);
                }
                else
                {
                   // Toast.makeText(PostSingleActivity.this, "im here", Toast.LENGTH_SHORT).show();
                    delete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String post_uid = (String) dataSnapshot.child("userId").getValue();
                        if(mAuth.getCurrentUser().getUid().equals(post_uid))
                        {
                            mDatabase.child(mPost_key).removeValue();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        //Toast.makeText(PostSingleActivity.this, mPost_key, Toast.LENGTH_LONG).show();
        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_title = (String) dataSnapshot.child("Title").getValue();
                String post_seat = (String) dataSnapshot.child("Seats").getValue();
                String post_rentperhour = (String) dataSnapshot.child("RentPerHour").getValue();
                String post_rentperday = (String) dataSnapshot.child("Rent per day local").getValue();
                String post_rentperdayoutside = (String) dataSnapshot.child("Rent per day outside").getValue();
                String post_image = (String) dataSnapshot.child("Image").getValue();
                String post_uid = (String) dataSnapshot.child("userId").getValue();

                mSinglePostTitle.setText(post_title);
                mSinglePostSeat.setText(post_seat);
                mSinglePostSeat.setText(post_seat);
                mSinglePostRentPerHour.setText(post_rentperhour);
                mSinglePostRentPerDay.setText(post_rentperday);
                mSinglePostRentPerDayOutside.setText(post_rentperdayoutside);

                Picasso.with(PostSingleActivity.this).load(post_image).into(mSinglePostImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String destination = spinner.getSelectedItem().toString();
                switch (destination) {
                    case "Select Destination":
                        break;
                    case "Bisnakandi":
                        clac("Bisnakandi");
                        break;

                    case "Moulvibazar":
                        clac("Moulvibazar");
                        break;

                    case "Sunamgonj":
                        clac("Sunamgonj");
                        break;

                    case "Beanibazar":
                        clac("Beanibazar");
                        break;

                    case "Lalakhal":
                        clac("Lalakhal");
                        break;

                    case "Srimongal":
                        clac("Srimongal");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void clac(String place) {

        Toast.makeText(this, "" + place, Toast.LENGTH_SHORT).show();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Place");
        myRef.child(place).child("distance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue().toString();
                Toast.makeText(PostSingleActivity.this, "" + data, Toast.LENGTH_SHORT).show();
                final double dist = Double.parseDouble(data);
                mDatabase.child(mPost_key).child("RentPerKm").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String data = dataSnapshot.getValue().toString();
                        double rent = Double.parseDouble(data);

                        TextView text = findViewById(R.id.showClc);

                        double res = rent * dist;

                        text.setText("Your Cost " + (int) res + "Tk");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
