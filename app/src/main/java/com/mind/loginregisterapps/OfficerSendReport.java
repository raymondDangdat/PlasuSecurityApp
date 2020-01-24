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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mind.loginregisterapps.Model.OfficerSendReportModel;

import java.util.Date;

public class OfficerSendReport extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference users, reports;
    private String uId = "";
    
    private EditText editTextReportTitle, editTextReportContent;
    private Button buttonSendReport;

    private String name, phoneNumber = "";

    private ProgressDialog reportProgressDialog;

    private OfficerSendReportModel newReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_send_report);

        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        users = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("users");
        reports = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("reports");


        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();

        reportProgressDialog = new ProgressDialog(this);
        
        editTextReportContent = findViewById(R.id.edt_report_body);
        editTextReportTitle = findViewById(R.id.edt_report_title);
        buttonSendReport = findViewById(R.id.btn_send_report);
        buttonSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport();
            }
        });
        getOfficerDetails();
    }
    private void getOfficerDetails() {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child(uId).child("username").getValue(String.class);
                phoneNumber = dataSnapshot.child(uId).child("phone").getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void sendReport() {
        reportProgressDialog.setMessage("Sending report...");
        reportProgressDialog.setTitle("Duty Report");

        final long reportDate = new Date().getTime();
        final String title = editTextReportTitle.getText().toString().trim();
        final String report = editTextReportContent.getText().toString().trim();

        if (TextUtils.isEmpty(report)){
            Toast.makeText(this, "Please type a valid report", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(title)){
            Toast.makeText(this, "Please type title of report", Toast.LENGTH_SHORT).show();
        }else {
            reportProgressDialog.show();
            newReport = new OfficerSendReportModel(uId, report, phoneNumber, title, name, reportDate);
            reports.push().setValue(newReport).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    reportProgressDialog.dismiss();
                    Toast.makeText(OfficerSendReport.this, "Report sent successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OfficerSendReport.this, OfficerReportsActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    reportProgressDialog.dismiss();
                    Toast.makeText(OfficerSendReport.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
