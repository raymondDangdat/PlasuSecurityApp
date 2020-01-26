package com.mind.loginregisterapps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mind.loginregisterapps.Common.Common;
import com.mind.loginregisterapps.Interface.ItemClickListener;
import com.mind.loginregisterapps.Model.OfficerModel;
import com.mind.loginregisterapps.Model.OfficerSendReportModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageOfficersActivity extends AppCompatActivity {
    private DatabaseReference officers;

    private OfficerModel officerModel;


    private FirebaseRecyclerAdapter<OfficerModel, ViewHolder>adapter;

    private RecyclerView recycler_officers;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_officers);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        officers = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("users");

        recycler_officers = findViewById(R.id.recycler_officers);
        recycler_officers.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_officers.setLayoutManager(layoutManager);

        loadOfficers();


    }

    private void loadOfficers() {
        FirebaseRecyclerOptions<OfficerModel>options = new FirebaseRecyclerOptions.Builder<OfficerModel>()
                .setQuery(officers, OfficerModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<OfficerModel, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull OfficerModel model) {
                holder.officerPhone.setText("Phone: " + model.getPhone());
                holder.officerEmail.setText("Email: " + model.getEmail());
                holder.officerName.setText("Name: " + model.getUsername());

                Picasso.get().load(model.getProfile_pix()).placeholder(R.drawable.images).into(holder.profilePix);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get complaint id to new activity
                        Intent assignDuty = new Intent(ManageOfficersActivity.this, AssignDutyActivity.class);
                        assignDuty.putExtra("OfficerId", adapter.getRef(position).getKey());
                        startActivity(assignDuty);
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.officers_layout, viewGroup,false);
                ViewHolder viewHolder = new ViewHolder(view);
                return viewHolder;
            }
        };
        recycler_officers.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.CALL)){
            call(adapter.getRef(item.getOrder()).getKey());
        }else if (item.getTitle().equals(Common.DELETE)){
            deleteOfficer(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteOfficer(String key) {
        officers.child(key).removeValue();
        Toast.makeText(this, "Officer deleted!", Toast.LENGTH_SHORT).show();

    }

    private void call(String key) {
        officers.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                officerModel = dataSnapshot.getValue(OfficerModel.class);
                final String phone = officerModel.getPhone().toString();
                //Toast.makeText(ManageOfficersActivity.this, phone, Toast.LENGTH_SHORT).show();
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:"+phone));
                if (ActivityCompat.checkSelfPermission(ManageOfficersActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(phoneIntent);
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


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,  View.OnCreateContextMenuListener {

        TextView officerName, officerEmail, officerPhone;
        CircleImageView profilePix;
        private ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            officerName = itemView.findViewById(R.id.txt_officer_name);
            officerEmail = itemView.findViewById(R.id.txt_email);
            officerPhone = itemView.findViewById(R.id.txt_phone);
            profilePix = itemView.findViewById(R.id.img_profile_pix);

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
            menu.add(0,0, getAdapterPosition(), Common.CALL);
            menu.add(0,1, getAdapterPosition(), Common.DELETE);
        }
    }
}
