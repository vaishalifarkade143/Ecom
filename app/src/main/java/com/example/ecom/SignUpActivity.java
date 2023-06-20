package com.example.ecom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ecom.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity
{
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //text click
        binding.goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            }
        });

        //for button click
        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.logName.getText().toString();
                String email = binding.logEmail.getText().toString();
                String pass = binding.logPass.getText().toString();

                createAccount(name,email,pass);
            }
        });
    }

    private void createAccount(String name, String email, String pass)
    {
       FirebaseAuth Fauth = FirebaseAuth.getInstance();

       //to show the  ProgressDialog while creating account
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating");
        progressDialog.setMessage("Account");
        progressDialog.show();
        //====

       Fauth.createUserWithEmailAndPassword(email.trim(),pass.trim())
               .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                   @Override
                   public void onSuccess(AuthResult authResult) {

                       //to crate an account
                       UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                               .setDisplayName(name).build();
                       FirebaseAuth.getInstance().getCurrentUser().updateProfile(userProfileChangeRequest);

                       //to cancle progressDialog after creating accoount
                       progressDialog.cancel();

                       //to show account created massage to user
                       Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                       //after creating account to make field empty
                       binding.logName.setText("");
                       binding.logEmail.setText("");
                       binding.logPass.setText("");
                   }
               })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       progressDialog.cancel();
                       Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               });
    }
}