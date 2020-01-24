package com.mind.loginregisterapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class OfficerSettingsActivity extends AppCompatActivity {

    private StorageReference mStorageImage;
    private FirebaseAuth mAuth;
    private DatabaseReference users;

    private String uId, userEmail = "";

    private static final int GALLERY_REQUEST_CODE = 1;
    private Uri mImageUri = null;

    private ProgressDialog settingsDialog;

    private EditText editTextName, editTextEmail, editTextPhone, editTextAddress;
    private CircleImageView profilePix;

    private Button buttonUpdate, buttonChangePassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_settings);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingsDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mStorageImage = FirebaseStorage.getInstance().getReference().child("plasuSecurityApp").child("profilePix");
        users = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("users");

        uId  = mAuth.getCurrentUser().getUid();

        editTextAddress = findViewById(R.id.edt_address);
        editTextEmail = findViewById(R.id.edt_email);
        editTextName = findViewById(R.id.edt_full_name);
        editTextPhone = findViewById(R.id.edt_phone);
        buttonUpdate = findViewById(R.id.btn_update_profile);
        profilePix = findViewById(R.id.profile_pix);
        buttonChangePassword = findViewById(R.id.btn_changepassword);




        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfilePix();
            }
        });


        profilePix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePix();
            }
        });
        
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });



        getUserDetails();

    }

    private void changePassword() {
        userEmail = mAuth.getCurrentUser().getEmail();
        settingsDialog.setMessage("Sending password reset link to " + userEmail);
        settingsDialog.show();


        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    settingsDialog.dismiss();
                    Toast.makeText(OfficerSettingsActivity.this, "Reset email sent to "+ userEmail, Toast.LENGTH_SHORT).show();
                    //logout
                    Intent signIn = new Intent(OfficerSettingsActivity.this, MainActivity.class);
                    signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mAuth.signOut();
                    startActivity(signIn);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                settingsDialog.dismiss();
                Toast.makeText(OfficerSettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(OfficerSettingsActivity.this, OfficerHome.class));
                finish();

            }
        });
    }

    private void getUserDetails() {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String name_retrieved = dataSnapshot.child(uId).child("username").getValue(String.class);
                final String email_retrieved = dataSnapshot.child(uId).child("email").getValue(String.class);
                final String phone_retrieved = dataSnapshot.child(uId).child("phone").getValue(String.class);
                final String profile_pix_data = dataSnapshot.child(uId).child("profile_pix").getValue(String.class);
                final String address = dataSnapshot.child(uId).child("address").getValue(String.class);

                editTextPhone.setText(phone_retrieved);
                editTextName.setText(name_retrieved);
                editTextEmail.setText(email_retrieved);
                editTextAddress.setText(address);
                Picasso.get().load(profile_pix_data).placeholder(R.drawable.images).into(profilePix);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateProfilePix() {
        if (mImageUri != null){
            final String address = editTextAddress.getText().toString().trim();
            final String phone = editTextPhone.getText().toString().trim();
            settingsDialog.setMessage("Updating profile picture...");
            settingsDialog.show();
            StorageReference filepath = mStorageImage.child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String profile_url = taskSnapshot.getDownloadUrl().toString();

                    users.child(uId).child("address").setValue(address);
                    users.child(uId).child("phone").setValue(phone);
                    users.child(uId).child("profile_pix").setValue(profile_url).addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    settingsDialog.dismiss();
                                    Toast.makeText(OfficerSettingsActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(OfficerSettingsActivity.this, OfficerHome.class));
                                    finish();
                                }
                            }
                    ).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            settingsDialog.dismiss();
                            Toast.makeText(OfficerSettingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(OfficerSettingsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            final String address = editTextAddress.getText().toString().trim();
            final String phone = editTextPhone.getText().toString().trim();
            if (TextUtils.isEmpty(address) || TextUtils.isEmpty(phone)){
                Toast.makeText(this, "Please include your phone number and address", Toast.LENGTH_SHORT).show();
            }else {
                settingsDialog.setMessage("Updating info...");
                settingsDialog.show();

                users.child(uId).child("phone").setValue(phone);
                users.child(uId).child("address").setValue(address).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        settingsDialog.dismiss();
                        Toast.makeText(OfficerSettingsActivity.this, "Info updated!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OfficerSettingsActivity.this, OfficerHome.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OfficerSettingsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            
        }
    }

    private void uploadProfilePix() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    //to set it to square
                    .setAspectRatio(4,4)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                profilePix.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
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
