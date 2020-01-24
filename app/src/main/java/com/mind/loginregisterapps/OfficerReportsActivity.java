package com.mind.loginregisterapps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mind.loginregisterapps.Interface.ItemClickListener;
import com.mind.loginregisterapps.Model.OfficerSendReportModel;
import com.mind.loginregisterapps.Utils.ReportUtils;

public class OfficerReportsActivity extends AppCompatActivity {
    private DatabaseReference users, reports;
    private String uId = "";
    private FirebaseAuth mAuth;

    private FirebaseRecyclerAdapter<OfficerSendReportModel, ViewHolder>adapter;

    private RecyclerView recycler_reports;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_reports);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        users = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("users");
        reports = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("reports");


        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();



        recycler_reports = findViewById(R.id.recycler_reports);
        recycler_reports.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_reports.setLayoutManager(layoutManager);
        
        
        loadReports();
    }

    private void loadReports() {
        //Toast.makeText(this, uId, Toast.LENGTH_SHORT).show();
        FirebaseRecyclerOptions<OfficerSendReportModel>options = new FirebaseRecyclerOptions.Builder<OfficerSendReportModel>()
                .setQuery(reports.orderByChild("uId").equalTo(uId), OfficerSendReportModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<OfficerSendReportModel, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull OfficerSendReportModel model) {
                holder.reportTitle.setText(model.getReportTitle());
                holder.reportDate.setText(ReportUtils.dateFromLong(model.getDate()));

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get complaint id to new activity
                        Intent reportDetail = new Intent(OfficerReportsActivity.this, OfficerReportDetails.class);
                        reportDetail.putExtra("reportId", adapter.getRef(position).getKey());
                        startActivity(reportDetail);
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.report_layout, viewGroup,false);
                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            }
        };
        recycler_reports.setAdapter(adapter);
        adapter.startListening();
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



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView reportTitle, reportDate;
        private ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reportTitle = itemView.findViewById(R.id.txt_report_title);
            reportDate = itemView.findViewById(R.id.txt_report_date);


            itemView.setOnClickListener(this);
        }


        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
