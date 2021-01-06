package com.gamecodeschool.navigation;

/**
 * Created by Asif Sobhan on 09-11-17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MyPost extends Fragment {

    private RecyclerView mPostList;
    private DatabaseReference mDatabase;
    private Query mData;
    private LinearLayoutManager mLayoutManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private String myID;

    private DatabaseReference likeDatabase;
    boolean mLike,mdLike;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_post, null);

        mPostList = (RecyclerView) view.findViewById(R.id.mypost_list);
        //mPostList.setHasFixedSize(true);
        firebaseAuth = FirebaseAuth.getInstance();
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mPostList.setLayoutManager(mLayoutManager);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null)
        {
            myID=currentUser.getUid();
            reload();
        }


    }

    public void reload() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mData=mDatabase.orderByChild("userId").equalTo(myID);
        likeDatabase = FirebaseDatabase.getInstance().getReference().child("like");

        FirebaseRecyclerAdapter<Post, PrimaryFragment.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PrimaryFragment.PostViewHolder>(
                Post.class,
                R.layout.post_row,
                PrimaryFragment.PostViewHolder.class,
                mData
        ) {
            @Override
            protected void populateViewHolder(final PrimaryFragment.PostViewHolder viewHolder, final Post model, int position) {
                final String post_key = getRef(position).getKey();
                final String userId = model.getUserId();
                final boolean isAvail = model.isAvailable();
                if (userId.equals(myID)) {
                    if (isAvail) {
                        viewHolder.avail.setText("Set unavailable");
                    } else {
                        viewHolder.avail.setText("Set available");
                    }
                    viewHolder.availability.setVisibility(View.GONE);
                    viewHolder.avail.setVisibility(View.VISIBLE);
                }
                else {
                    if (isAvail) {
                        viewHolder.availability.setText("Available");
                    } else {
                        viewHolder.availability.setText("Unavailable");
                    }
                    viewHolder.availability.setVisibility(View.VISIBLE);
                    viewHolder.avail.setVisibility(View.GONE);
                }

                likeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(post_key).hasChild(userId))
                        {
                            viewHolder.like1.setVisibility(View.GONE);
                            viewHolder.like2.setVisibility(View.VISIBLE);
                            viewHolder.like1.setEnabled(false);
                            viewHolder.like2.setEnabled(true);
                        }
                        else
                        {
                            viewHolder.like1.setVisibility(View.VISIBLE);
                            viewHolder.like2.setVisibility(View.GONE);
                            viewHolder.like2.setEnabled(false);
                            viewHolder.like1.setEnabled(true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.like1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mLike = true;
                        likeDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                final DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");


                                if (mLike) {
                                    myDatabase.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(mLike)
                                            {
                                                String post_rate = dataSnapshot.child("rate").getValue().toString();
                                                int a = Integer.parseInt(post_rate);
                                                a++;
                                                Toast.makeText(getContext(), "like", Toast.LENGTH_SHORT).show();
                                                myDatabase.child(post_key).child("rate").setValue(String.valueOf(a));
                                                likeDatabase.child(post_key).child(userId).setValue("liked");
                                                mLike = false;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });


                viewHolder.like2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mdLike = true;
                        likeDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                final DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");


                                if (mdLike) {
                                    myDatabase.child(post_key).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(mdLike)
                                            {
                                                String post_rate = dataSnapshot.child("rate").getValue().toString();
                                                int a = Integer.parseInt(post_rate);
                                                a--;
                                                Toast.makeText(getContext(), "dislike", Toast.LENGTH_SHORT).show();
                                                myDatabase.child(post_key).child("rate").setValue(String.valueOf(a));
                                                likeDatabase.child(post_key).child(userId).removeValue();
                                                mdLike = false;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

               /* viewHolder.like2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       if(mLike)
                       {
                           likeDatabase.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {

                                   final DatabaseReference myDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
                                   myDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                           String post_rate = dataSnapshot.child("rate").getValue().toString();
                                           int a = Integer.parseInt(post_rate);
                                           a--;

                                           Toast.makeText(getContext(), "unlike", Toast.LENGTH_SHORT).show();

                                           myDatabase.child(post_key).child("rate").setValue(a);
                                           likeDatabase.child(post_key).child(userId).removeValue();
                                           viewHolder.like1.setVisibility(View.VISIBLE);
                                           viewHolder.like2.setVisibility(View.GONE);
                                           viewHolder.like1.setText(String.valueOf(a));
                                           mLike=false;
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
                });*/

                viewHolder.setTitle(model.getTitle());
                viewHolder.setRate(model.getRate());
                viewHolder.setDesc(model.getRentPerHour());
                viewHolder.setSeats(model.getSeats());
                viewHolder.setImage(getActivity().getApplicationContext(), model.getImage());
                viewHolder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singlePostIntent = new Intent(getActivity(), PostSingleActivity.class);
                        singlePostIntent.putExtra("post_id", post_key);
                        startActivity(singlePostIntent);
                    }
                });

                viewHolder.avail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Posts").child(post_key);
                        if (model.isAvailable()) {
                            myRef.child("available").setValue(false);
                            // viewHolder.avail.setText("Set available");
                        } else {
                            myRef.child("available").setValue(true);
                            // viewHolder.avail.setText("Set unavailable");

                        }
                    }
                });


            }
        };

        mPostList.setAdapter(firebaseRecyclerAdapter);
    }


}