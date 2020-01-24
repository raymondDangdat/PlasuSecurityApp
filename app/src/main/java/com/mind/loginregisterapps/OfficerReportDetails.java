package com.mind.loginregisterapps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mind.loginregisterapps.Model.OfficerSendReportModel;
import com.mind.loginregisterapps.Utils.ReportUtils;

public class OfficerReportDetails extends AppCompatActivity {
    String reportId = "";
    private DatabaseReference users, reports;
    private TextView txtReportTitle, txtReport, txtReportDate;

    private OfficerSendReportModel sendReportModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_report_details);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        users = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("users");
        reports = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("reports");

        txtReport = findViewById(R.id.tvReport);
        txtReportDate = findViewById(R.id.tvReportDate);
        txtReportTitle = findViewById(R.id.tvReportTitle);

        //get report id from Intent
        if (getIntent() != null){
            reportId = getIntent().getStringExtra("reportId");

            if (!reportId.isEmpty()){
                getreportDetails(reportId);

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

    private void getreportDetails(String reportId) {
        reports.child(reportId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sendReportModel = dataSnapshot.getValue(OfficerSendReportModel.class);
                txtReport.setText(sendReportModel.getReport());
                txtReportTitle.setText(sendReportModel.getReportTitle());
                txtReportDate.setText(ReportUtils.dateFromLong(sendReportModel.getDate()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
