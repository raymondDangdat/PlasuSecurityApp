package com.mind.loginregisterapps;

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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mind.loginregisterapps.Interface.ItemClickListener;
import com.mind.loginregisterapps.Model.DutyRoosterModel;

public class DutyRooster extends AppCompatActivity {
    private DatabaseReference duty;

    private RecyclerView recycler_duty_rooster;
    RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<DutyRoosterModel, ViewHolder>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_rooster);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        duty = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("duty");

        recycler_duty_rooster = findViewById(R.id.recycler_duty_rooster);
        recycler_duty_rooster.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_duty_rooster.setLayoutManager(layoutManager);


        loadDutyRooster();

    }

    private void loadDutyRooster() {
        FirebaseRecyclerOptions<DutyRoosterModel> options = new FirebaseRecyclerOptions.Builder<DutyRoosterModel>()
                .setQuery(duty, DutyRoosterModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<DutyRoosterModel, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull DutyRoosterModel model) {
                holder.sunday.setText("Sunday: " + model.getSunday());
                holder.monday.setText("Monday: " + model.getMonday());
                holder.tuesday.setText("Tuesday: " + model.getTuesday());
                holder.wednesday.setText("Wednesday: " + model.getWednesday());
                holder.thursday.setText("Thursday: " + model.getThursday());
                holder.friday.setText("Friday: " + model.getFriday());
                holder.saturday.setText("Saturday: " + model.getSaturday());
                holder.sunday.setText("Sunday: " + model.getSunday());
                holder.officerName.setText("Officer's Name: " + model.getOfficerName());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.duty_rooster_layout, viewGroup,false);
                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            }
        };
        recycler_duty_rooster.setAdapter(adapter);
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
        TextView officerName, monday, tuesday, wednesday, thursday, friday, saturday, sunday;
        private ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            officerName = itemView.findViewById(R.id.txt_officer_name);
            monday = itemView.findViewById(R.id.txt_monday);
            tuesday = itemView.findViewById(R.id.txt_tuesday);
            wednesday = itemView.findViewById(R.id.txt_wednesday);
            thursday = itemView.findViewById(R.id.txt_thursday);
            friday = itemView.findViewById(R.id.txt_friday);
            saturday = itemView.findViewById(R.id.txt_saturday);
            sunday = itemView.findViewById(R.id.txt_sunday);


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
