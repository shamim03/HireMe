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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmailLogin;
    private EditText etPasswordLogin;
    private Button bLogin;
    private TextView tvForgotPasword;
    private TextView tvRegisterHere;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        }

        etEmailLogin = (EditText) findViewById(R.id.etEmailLogin);
        etPasswordLogin = (EditText) findViewById(R.id.etPasswordLogin);
        bLogin = (Button) findViewById(R.id.bLogin);
        tvRegisterHere = (TextView) findViewById(R.id.tvRegisterHere);
        tvForgotPasword = (TextView) findViewById(R.id.tvForgotPassword);
        bLogin.setOnClickListener(this);
        tvRegisterHere.setOnClickListener(this);
        tvForgotPasword.setOnClickListener(this);


    }
    public void userLogin() {
        String email = etEmailLogin.getText().toString().trim();
        String password = etPasswordLogin.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //Email is Emty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;

        }
        progressDialog.setMessage("Signing in....");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else{
                    Toast.makeText(LoginActivity.this, "Please Register First", Toast.LENGTH_SHORT).show();
                    return;

                }


            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == bLogin){
            userLogin();
        }
        if(view == tvRegisterHere){
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        }
        if(view == tvForgotPasword){
            finish();
            startActivity(new Intent(this, ForgotPassword.class));
        }

    }

}
