package com.mind.loginregisterapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private RelativeLayout rlayout;
    private Animation animation;

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextCPassword;
    private Button btnRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference users;

    private String uId = "";

    private ProgressDialog registerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this,R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);

        registerDialog = new ProgressDialog(this);

        btnRegister = findViewById(R.id.btn_register);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextCPassword = findViewById(R.id.editTextCPassword);
        editTextUsername = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);

        mAuth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOfficer();
            }
        });
    }

    private void addOfficer() {
        registerDialog.setTitle("Creating New Account");
        registerDialog.setMessage("Account creation in progress...");

        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String cPassword = editTextCPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please enter officer's name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email field cannot be empty", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password field cannot be empty", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(cPassword)){
            Toast.makeText(this, "Please confirm password.", Toast.LENGTH_SHORT).show();
        }else if (!password.equals(cPassword)){
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
        }else {
            registerDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        uId = mAuth.getCurrentUser().getUid();
                        users.child(uId).child("email").setValue(email);
                        users.child(uId).child("uId").setValue(uId);
                        users.child(uId).child("username").setValue(username).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        registerDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, ManageOfficersActivity.class));
                                    }
                                }
                        ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                registerDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    registerDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
