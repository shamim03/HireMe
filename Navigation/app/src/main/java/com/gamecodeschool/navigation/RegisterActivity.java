package com.gamecodeschool.navigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.gamecodeschool.navigation.R.id.etName;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private Button bRegister;
    private TextView tvLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }
        progressDialog = new ProgressDialog(this);

        bRegister = (Button) findViewById(R.id.bRegister);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        TextView loginLink = (TextView) findViewById(R.id.tvLogin);
        bRegister.setOnClickListener(this);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(loginIntent);
            }
        });

    }
    public void registerUser(){
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //Email is Emty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;

        }
        progressDialog.setMessage("Registering User....");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String user_id = firebaseAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = mDatabase.child(user_id);
                    current_user_db.child("name").setValue(name);
                    current_user_db.child("email").setValue(email);
                    current_user_db.child("image").setValue("default");
                    //finish();
                    startActivity(new Intent(getApplicationContext(),SetupActivity.class));
                }
                /*else{
                    Toast.makeText(RegisterActivity.this, "Registration Problem, Try Again!", Toast.LENGTH_SHORT).show();

                }*/
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == bRegister){
            registerUser();
        }

    }
}
