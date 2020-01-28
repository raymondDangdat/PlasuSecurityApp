package com.mind.loginregisterapps;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity{

    private ImageButton btRegister;
    private TextView tvLogin, tvForgotPassword;
    private Button btnLogin;
    private EditText editTextEmail, editTextPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference users;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btRegister  = findViewById(R.id.btRegister);
        tvLogin     = findViewById(R.id.tvLogin);
        //btRegister.setOnClickListener(this);
        btnLogin = findViewById(R.id.btn_login);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        tvForgotPassword = findViewById(R.id.tvForgot);

        mAuth = FirebaseAuth.getInstance();



        progressDialog = new ProgressDialog(this);

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Problem on ground!", Toast.LENGTH_SHORT).show();
                loginOfficer();
            }
        });
    }

    private void loginOfficer() {
        progressDialog.setMessage("Login in...");
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email field is empty", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Type your password", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        if (email.equals("chiefsecurityplasu@gmail.com")){
                            Intent loginIntent = new Intent(MainActivity.this, ChiefSecurity.class);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginIntent);
                            finish();
                        }else{
                            Intent loginIntent = new Intent(MainActivity.this,OfficerHome.class);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginIntent);
                            finish();
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public void onClick(View v) {
//        if (v==btRegister){
//            Intent intent   = new Intent(MainActivity.this,RegisterActivity.class);
//            Pair[] pairs    = new Pair[1];
//            pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
//            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
//            startActivity(intent,activityOptions.toBundle());
//        }
//    }
}
