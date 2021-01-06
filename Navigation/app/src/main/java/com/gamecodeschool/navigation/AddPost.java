package com.gamecodeschool.navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class AddPost extends Fragment {
    private static final int GELLERY_REQUEST = 1;
    private ImageButton mSelectImage;
    private EditText etTitle;
    private EditText etSeats;
    private EditText etRentPerHour;
    private EditText etRentPerDayLocal;
    private EditText etRentPerDayOutside;
    private EditText etRentPerKm;
    private EditText etPhoneNo;
    private Button bAddPost;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_addpost, null);
        mStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        bAddPost = (Button) view.findViewById(R.id.bAddPost);
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etSeats = (EditText) view.findViewById(R.id.etSeats);
        etPhoneNo = (EditText) view.findViewById(R.id.etPhoneNo);
        etRentPerHour = (EditText) view.findViewById(R.id.etRentPerHour);
        etRentPerDayLocal = (EditText) view.findViewById(R.id.etRentPerDayLocal);
        etRentPerDayOutside = (EditText) view.findViewById(R.id.etRentPerDayOutside);
        etRentPerKm = (EditText) view.findViewById(R.id.etRentPerKm);
        mProgress = new ProgressDialog(getActivity());


        mSelectImage = (ImageButton) view.findViewById(R.id.SelectImage);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gelleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                gelleryIntent.setType("image/*");
                startActivityForResult(gelleryIntent, GELLERY_REQUEST);
            }
        });

        bAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
        return view;


    }

    private void startPosting() {
        mProgress.setMessage("Posting....");

        final String titleVal = etTitle.getText().toString().trim();
        final String SeatsVal = etSeats.getText().toString().trim();
        final String RentPerHourVal = etRentPerHour.getText().toString().trim();
        final String RentPerDayLocalVal = etRentPerDayLocal.getText().toString().trim();
        final String RentPerDayOutsideVal = etRentPerDayOutside.getText().toString().trim();
        final String RentPerKmVal = etRentPerKm.getText().toString().trim();
        final String PhoneNoVal = etPhoneNo.getText().toString().trim();


        if (mImageUri!=null && checkPhone(PhoneNoVal) && checkSeat(SeatsVal) && !TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(SeatsVal) && !TextUtils.isEmpty(RentPerHourVal) &&
                !TextUtils.isEmpty(RentPerDayLocalVal) && !TextUtils.isEmpty(RentPerKmVal)) {
            mProgress.show();
            StorageReference filepath = mStorage.child("Post_images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();
                    newPost.child("Title").setValue(titleVal);
                    newPost.child("Seats").setValue(SeatsVal);
                    newPost.child("RentPerHour").setValue(RentPerHourVal);
                    newPost.child("Rent per day local").setValue(RentPerDayLocalVal);
                    newPost.child("Rent per day outside").setValue(RentPerDayOutsideVal);
                    newPost.child("RentPerKm").setValue(RentPerKmVal);
                    newPost.child("Contact_No").setValue(PhoneNoVal);
                    newPost.child("Image").setValue(downloadUrl.toString());
                    newPost.child("userId").setValue(userID);
                    newPost.child("available").setValue(true);
                    newPost.child("rate").setValue("0");


                    mProgress.dismiss();
                    getActivity().startActivity(new Intent(getActivity(), MainActivity.class));


                }
            });

        }
        else
        {
            Toast.makeText(getContext(), "Check Your Values", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkPhone(String phoneNoVal) {

        if(phoneNoVal.length()!=11)
            return false;
        else return true;
    }
    private boolean checkSeat(String SeatsVal) {

        if(SeatsVal.length() >2)
            return false;
        else return true;
    }




    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GELLERY_REQUEST && resultCode == RESULT_OK) {

            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null)
        {
            userID=currentUser.getUid();
        }

    }
}
