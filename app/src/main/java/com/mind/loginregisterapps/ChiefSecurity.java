package com.mind.loginregisterapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mind.loginregisterapps.Interface.ItemClickListener;
import com.mind.loginregisterapps.Model.NoticeModel;
import com.mind.loginregisterapps.Utils.ReportUtils;

import java.util.Date;

public class ChiefSecurity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;

    private DatabaseReference notice;
    private ProgressDialog dialog;

    private FirebaseRecyclerAdapter<NoticeModel, ViewHolder>adapter;
    private RecyclerView recycler_notices;
    RecyclerView.LayoutManager layoutManager;

    private NoticeModel newNotice;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chief_security);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);

        notice = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("notices");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayNoticeDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        recycler_notices = findViewById(R.id.recycler_notice);
        recycler_notices.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_notices.setLayoutManager(layoutManager);


        loadNotices();
    }

    private void loadNotices() {
        FirebaseRecyclerOptions<NoticeModel>options = new FirebaseRecyclerOptions.Builder<NoticeModel>()
                .setQuery(notice, NoticeModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<NoticeModel, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull NoticeModel model) {
                holder.noticeDate.setText(ReportUtils.dateFromLong(model.getDate()));
                holder.noticeTitle.setText(model.getNoticeTitle());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get complaint id to new activity
                        Intent reportDetail = new Intent(ChiefSecurity.this, NoticeDetails.class);
                        reportDetail.putExtra("noticeId", adapter.getRef(position).getKey());
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
        recycler_notices.setAdapter(adapter);
        adapter.startListening();
    }

    private void displayNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChiefSecurity.this);
        View view = getLayoutInflater().inflate(R.layout.notification_dialog, null);

        final EditText editTextNoteTitle = view.findViewById(R.id.edit_notice_title);
        final EditText editTextNoticeContent = view.findViewById(R.id.edit_notice_content);
        Button btn_send = view.findViewById(R.id.btn_send);

        //set onclick listener on login button
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String noteTitle = editTextNoteTitle.getText().toString().trim();
                final String noteContent = editTextNoticeContent.getText().toString().trim();

                final long noticeDate = new Date().getTime();

                if (TextUtils.isEmpty(noteTitle)){
                    Toast.makeText(ChiefSecurity.this, "Please enter notice title", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(noteContent)){
                    Toast.makeText(ChiefSecurity.this, "Please enter note content", Toast.LENGTH_SHORT).show();
                }else {
                    dialog.setMessage("Sending notification...");
                    dialog.show();
                    newNotice = new NoticeModel(noteTitle, noteContent, noticeDate);

                    notice.push().setValue(newNotice).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                dialog.dismiss();
                                Toast.makeText(ChiefSecurity.this, "Notice snet successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ChiefSecurity.this, ChiefSecurity.class));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChiefSecurity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chief_security, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_add_officer) {
            startActivity(new Intent(ChiefSecurity.this, RegisterActivity.class));
        } else if (id == R.id.nav_duty_rooster) {
            startActivity(new Intent(ChiefSecurity.this, DutyRooster.class));

        } else if (id == R.id.nav_view_report) {
            startActivity(new Intent(ChiefSecurity.this, ChiefViewReportsActivity.class));

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(ChiefSecurity.this,ChangePasswordActivity.class));

        } else if (id == R.id.nav_exit) {
            mAuth.getCurrentUser();
            mAuth.signOut();
            finish();
            Intent signoutIntent = new Intent(ChiefSecurity.this, MainActivity.class);
            signoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signoutIntent);
            finish();

        }else if (id == R.id.nav_officers){
            startActivity(new Intent(ChiefSecurity.this, ManageOfficersActivity.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView noticeTitle, noticeDate;
        private ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noticeTitle = itemView.findViewById(R.id.txt_report_title);
            noticeDate = itemView.findViewById(R.id.txt_report_date);


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
