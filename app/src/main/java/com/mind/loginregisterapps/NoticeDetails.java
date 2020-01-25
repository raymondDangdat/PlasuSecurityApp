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
import com.mind.loginregisterapps.Model.NoticeModel;
import com.mind.loginregisterapps.Model.OfficerSendReportModel;
import com.mind.loginregisterapps.Utils.ReportUtils;

public class NoticeDetails extends AppCompatActivity {
    private DatabaseReference notice;
    private TextView txtNoticeTitle, txtNotice, txtNoticeDate;
    private String noticeId;
    private NoticeModel noticeModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNotice = findViewById(R.id.tvNotice);
        txtNoticeDate = findViewById(R.id.tvNoticeDate);
        txtNoticeTitle = findViewById(R.id.tvNoticeTitle);

        notice = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("notices");


        //get report id from Intent
        if (getIntent() != null){
            noticeId = getIntent().getStringExtra("noticeId");

            if (!noticeId.isEmpty()){
                getreportDetails(noticeId);

            }

        }




    }

    private void getreportDetails(String noticeId) {
        notice.child(noticeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noticeModel = dataSnapshot.getValue(NoticeModel.class);
                txtNotice.setText(noticeModel.getNoticeContent());
                txtNoticeTitle.setText(noticeModel.getNoticeTitle());
                txtNoticeDate.setText(ReportUtils.dateFromLong(noticeModel.getDate()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
