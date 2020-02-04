package com.mind.loginregisterapps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
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
import com.mind.loginregisterapps.Common.Common;
import com.mind.loginregisterapps.Interface.ItemClickListener;
import com.mind.loginregisterapps.Model.OfficerSendReportModel;
import com.mind.loginregisterapps.Utils.ReportUtils;

public class ChiefViewReportsActivity extends AppCompatActivity {
    private DatabaseReference reports;


    private FirebaseRecyclerAdapter<OfficerSendReportModel, ViewHolder> adapter;

    private RecyclerView recycler_reports;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chief_view_reports);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reports = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("reports");


        recycler_reports = findViewById(R.id.recycler_reports);
        recycler_reports.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_reports.setLayoutManager(layoutManager);


        loadReports();
    }

    private void loadReports() {
        FirebaseRecyclerOptions<OfficerSendReportModel> options = new FirebaseRecyclerOptions.Builder<OfficerSendReportModel>()
                .setQuery(reports.orderByChild("date"), OfficerSendReportModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<OfficerSendReportModel, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull OfficerSendReportModel model) {
                holder.reporterName.setText(model.getOfficerName());
                holder.reportDate.setText(ReportUtils.dateFromLong(model.getDate()));

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get complaint id to new activity
                        Intent reportDetail = new Intent(ChiefViewReportsActivity.this, ChiefReportDetailsActivity.class);
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
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)){
            deleteRooster(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteRooster(String key) {
        reports.child(key).removeValue();
        Toast.makeText(this, "Duty rooster deleted", Toast.LENGTH_SHORT).show();
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



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView reporterName, reportDate;
        private ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reporterName = itemView.findViewById(R.id.txt_report_title);
            reportDate = itemView.findViewById(R.id.txt_report_date);


            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an action");
            menu.add(0,1, getAdapterPosition(), Common.DELETE);
        }
    }
}
