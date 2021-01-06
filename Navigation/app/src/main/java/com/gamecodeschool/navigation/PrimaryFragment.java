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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class PrimaryFragment extends Fragment {
    private RecyclerView mPostList;
    private Query mDatabase;
    private LinearLayoutManager mLayoutManager;
    private Spinner spinner;
    private DatabaseReference likeDatabase;
    boolean mLike,mdLike;
    String myId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.primary_layout, null);
        mPostList = (RecyclerView) view.findViewById(R.id.post_list);
        //mPostList.setHasFixedSize(true);
        spinner = view.findViewById(R.id.seats_spn);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").orderByKey();
        likeDatabase = FirebaseDatabase.getInstance().getReference().child("like");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String seat = spinner.getSelectedItem().toString();
                switch (seat) {
                    case "Select":
                        break;
                    case "ALL":
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").orderByKey();
                        reload();
                        break;
                    case "4":
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("Seats").equalTo("4");
                        reload();
                        break;

                    case "5":
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("Seats").equalTo("5");
                        reload();
                        break;

                    case "6":
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("Seats").equalTo("6");
                        reload();
                        break;

                    case "7":
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("Seats").equalTo("7");
                        reload();
                        break;

                    case "8":
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("Seats").equalTo("8");
                        reload();
                        break;

                    case "9":
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("Seats").equalTo("9");
                        reload();
                        break;

                    case "10":
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("Seats").equalTo("10");
                        reload();
                        break;

                    case "11":
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("Seats").equalTo("11");
                        reload();
                        break;

                    case "12":
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("Seats").equalTo("12");
                        reload();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mPostList.setLayoutManager(mLayoutManager);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        myId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        reload();

    }

    public void reload() {
        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.post_row,
                PostViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Post model, int position) {
                final String post_key = getRef(position).getKey();
                final String userId = model.getUserId();
                final boolean isAvail = model.isAvailable();
                if (userId.equals(myId)) {
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
                        if(dataSnapshot.child(post_key).hasChild(myId))
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
                                                likeDatabase.child(post_key).child(myId).setValue("liked");
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
                                                likeDatabase.child(post_key).child(myId).removeValue();
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

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button avail, like1, like2;
        ImageView image;
        TextView availability;

        public PostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            avail = itemView.findViewById(R.id.btn_avail);
            availability = itemView.findViewById(R.id.availability);
            image = itemView.findViewById(R.id.post_image);
            like1 = itemView.findViewById(R.id.likeBtn1);
            like2 = itemView.findViewById(R.id.likeBtn2);

        }

        public void setTitle(String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setRate(String rate) {
            TextView post_title = (TextView) mView.findViewById(R.id.likeBtn1);
            TextView post_title2 = (TextView) mView.findViewById(R.id.likeBtn2);
            post_title.setText(rate);
            post_title2.setText(rate);
        }

        public void setDesc(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);

        }

        public void setSeats(String seats) {
            TextView post_seat = (TextView) mView.findViewById(R.id.post_seat);
            post_seat.setText(seats);

        }

        public void setImage(Context ctx, String Image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(Image).into(post_image);
        }
    }
}
