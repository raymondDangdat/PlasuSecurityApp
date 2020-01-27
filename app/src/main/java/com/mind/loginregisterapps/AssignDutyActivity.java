package com.mind.loginregisterapps;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mind.loginregisterapps.Model.OfficerModel;

public class AssignDutyActivity extends AppCompatActivity {
    private String officerId;

    private DatabaseReference officers, duty;
    private String officerName = "";

    private OfficerModel officerModel;

    private Button btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday, btnSunday;

    private Spinner smds, stds, swds, sthds, sfds, ssds, ssuds;
    private Spinner spinnerMonDutyPost, spinnerMonDutyTime;
    private Spinner spinnerTuesDutyPost, spinnerTuesDutyTime;
    private Spinner spinnerWedDutyPost, spinnerWedDutyTime;
    private Spinner spinnerThurDutyPost, spinnerThurDutyTime;
    private Spinner spinnerFriDutyPost, spinnerFriDutyTime;
    private Spinner spinnerSatDutyPost, spinnerSatDutyTime;
    private Spinner spinnerSunDutyPost, spinnerSunDutyTime;
    private Button buttonSaveMonday, buttonSaveTuesday, buttonSaveWed, buttonSaveThur, buttonSaveFri, buttonSaveSat, buttonSaveSun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_duty);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        officers = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("users");
        duty = FirebaseDatabase.getInstance().getReference().child("plasuSecurityApp").child("duty");


        //get report id from Intent
        if (getIntent() != null){
            officerId = getIntent().getStringExtra("officerId");

            if (!officerId.isEmpty()){

                //get the username
                officers.child(officerId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        officerName = dataSnapshot.child("username").getValue(String.class);

                        //try toasting the name to be sure it captures
                        //Toast.makeText(AssignDutyActivity.this, officerName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //Toast.makeText(this, officerId, Toast.LENGTH_SHORT).show();
                smds = findViewById(R.id.msds);
                stds = findViewById(R.id.tsds);
                swds = findViewById(R.id.wsds);
                sthds = findViewById(R.id.thsds);
                sfds = findViewById(R.id.fsds);
                ssds = findViewById(R.id.ssds);
                ssuds = findViewById(R.id.susds);

                smds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String status = smds.getSelectedItem().toString();
                        spinnerMonDutyPost = findViewById(R.id.msdp);
                        spinnerMonDutyTime = findViewById(R.id.msdt);
                        buttonSaveMonday = findViewById(R.id.buttonSaveMonday);


                        if (status.equals("Off Duty")){
                            spinnerMonDutyTime.setVisibility(View.INVISIBLE);
                            spinnerMonDutyPost.setVisibility(View.INVISIBLE);

                            //save off duty status for monday
                            buttonSaveMonday.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    duty.child(officerId).child("Monday").setValue("OFF DAY").addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        duty.child(officerId).child("officerName").setValue(officerName);
                                                        buttonSaveMonday.setEnabled(false);
                                                        Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                                }
                            });


                        }else {
                            spinnerMonDutyTime.setVisibility(View.VISIBLE);
                            spinnerMonDutyPost.setVisibility(View.VISIBLE);
                            buttonSaveMonday.setEnabled(true);


                            buttonSaveMonday.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String dutyTime = spinnerMonDutyTime.getSelectedItem().toString();
                                    String dutyPost = spinnerMonDutyPost.getSelectedItem().toString();
                                    final String dutyPostAndTime = dutyPost + " " + dutyTime;

                                    duty.child(officerId).child("Monday").setValue(dutyPostAndTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                duty.child(officerId).child("officerName").setValue(officerName);
                                                buttonSaveMonday.setEnabled(false);
                                                Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                stds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String status = stds.getSelectedItem().toString();

                        spinnerTuesDutyPost = findViewById(R.id.tsdp);
                        spinnerTuesDutyTime = findViewById(R.id.tsdt);
                        buttonSaveTuesday = findViewById(R.id.buttonSaveTuesday);


                        if (status.equals("Off Duty")){
                            spinnerTuesDutyTime.setVisibility(View.INVISIBLE);
                            spinnerTuesDutyPost.setVisibility(View.INVISIBLE);

                            //save off duty status for monday
                            buttonSaveTuesday.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    duty.child(officerId).child("Tuesday").setValue("OFF DAY").addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        duty.child(officerId).child("officerName").setValue(officerName);
                                                        buttonSaveTuesday.setEnabled(false);
                                                        Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                                }
                            });


                        }else {
                            spinnerTuesDutyTime.setVisibility(View.VISIBLE);
                            spinnerTuesDutyPost.setVisibility(View.VISIBLE);
                            buttonSaveTuesday.setEnabled(true);


                            buttonSaveTuesday.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String dutyTime = spinnerTuesDutyTime.getSelectedItem().toString();
                                    String dutyPost = spinnerTuesDutyPost.getSelectedItem().toString();
                                    final String dutyPostAndTime = dutyPost + " " + dutyTime;

                                    duty.child(officerId).child("Tuesday").setValue(dutyPostAndTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                duty.child(officerId).child("officerName").setValue(officerName);
                                                buttonSaveTuesday.setEnabled(false);
                                                Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                swds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String status = swds.getSelectedItem().toString();

                        spinnerWedDutyPost = findViewById(R.id.wsdp);
                        spinnerWedDutyTime = findViewById(R.id.wsdt);
                        buttonSaveWed = findViewById(R.id.buttonSaveWednesday);


                        if (status.equals("Off Duty")){
                            spinnerWedDutyTime.setVisibility(View.INVISIBLE);
                            spinnerWedDutyPost.setVisibility(View.INVISIBLE);

                            //save off duty status for monday
                            buttonSaveWed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    duty.child(officerId).child("Wednesday").setValue("OFF DAY").addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        duty.child(officerId).child("officerName").setValue(officerName);
                                                        buttonSaveWed.setEnabled(false);
                                                        Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                                }
                            });


                        }else {
                            spinnerWedDutyTime.setVisibility(View.VISIBLE);
                            spinnerWedDutyPost.setVisibility(View.VISIBLE);
                            buttonSaveWed.setEnabled(true);


                            buttonSaveWed.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String dutyTime = spinnerWedDutyTime.getSelectedItem().toString();
                                    String dutyPost = spinnerWedDutyPost.getSelectedItem().toString();
                                    final String dutyPostAndTime = dutyPost + " " + dutyTime;

                                    duty.child(officerId).child("Wednesday").setValue(dutyPostAndTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                duty.child(officerId).child("officerName").setValue(officerName);
                                                buttonSaveWed.setEnabled(false);
                                                Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sthds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String status = sthds.getSelectedItem().toString();

                        spinnerThurDutyPost = findViewById(R.id.thsdp);
                        spinnerThurDutyTime = findViewById(R.id.thsdt);
                        buttonSaveThur = findViewById(R.id.buttonSaveThursday);


                        if (status.equals("Off Duty")){
                            spinnerThurDutyPost.setVisibility(View.INVISIBLE);
                            spinnerThurDutyTime.setVisibility(View.INVISIBLE);

                            //save off duty status for monday
                            buttonSaveThur.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    duty.child(officerId).child("Thursday").setValue("OFF DAY").addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        duty.child(officerId).child("officerName").setValue(officerName);
                                                        buttonSaveThur.setEnabled(false);
                                                        Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                                }
                            });


                        }else {
                            spinnerThurDutyTime.setVisibility(View.VISIBLE);
                            spinnerThurDutyPost.setVisibility(View.VISIBLE);
                            buttonSaveThur.setEnabled(true);

                            buttonSaveThur.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String dutyTime = spinnerThurDutyTime.getSelectedItem().toString();
                                    String dutyPost = spinnerThurDutyPost.getSelectedItem().toString();
                                    final String dutyPostAndTime = dutyPost + " " + dutyTime;
                                    duty.child(officerId).child("Thursday").setValue(dutyPostAndTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                duty.child(officerId).child("officerName").setValue(officerName);
                                                buttonSaveThur.setEnabled(false);
                                                Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sfds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String status = sfds.getSelectedItem().toString();

                        spinnerFriDutyPost = findViewById(R.id.fsdp);
                        spinnerFriDutyTime = findViewById(R.id.fsdt);
                        buttonSaveFri = findViewById(R.id.buttonSaveFriday);


                        if (status.equals("Off Duty")){
                            spinnerFriDutyPost.setVisibility(View.INVISIBLE);
                            spinnerFriDutyTime.setVisibility(View.INVISIBLE);

                            //save off duty status for monday
                            buttonSaveFri.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    duty.child(officerId).child("Friday").setValue("OFF DAY").addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        buttonSaveFri.setEnabled(false);
                                                        Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                                }
                            });


                        }else {
                            spinnerFriDutyTime.setVisibility(View.VISIBLE);
                            spinnerFriDutyPost.setVisibility(View.VISIBLE);
                            buttonSaveFri.setEnabled(true);

                            buttonSaveFri.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String dutyTime = spinnerFriDutyTime.getSelectedItem().toString();
                                    String dutyPost = spinnerFriDutyPost.getSelectedItem().toString();
                                    final String dutyPostAndTime = dutyPost + " " + dutyTime;
                                    duty.child(officerId).child("Friday").setValue(dutyPostAndTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                        String status = sfds.getSelectedItem().toString();

                        spinnerFriDutyPost = findViewById(R.id.fsdp);
                        spinnerFriDutyTime = findViewById(R.id.fsdt);
                        buttonSaveFri = findViewById(R.id.buttonSaveFriday);


                        if (status.equals("Off Duty")){
                            spinnerFriDutyPost.setVisibility(View.INVISIBLE);
                            spinnerFriDutyTime.setVisibility(View.INVISIBLE);

                            //save off duty status for monday
                            buttonSaveFri.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    duty.child(officerId).child("Friday").setValue("OFF DAY").addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        duty.child(officerId).child("officerName").setValue(officerName);
                                                        buttonSaveFri.setEnabled(false);
                                                        Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                                }
                            });


                        }else {
                            spinnerFriDutyTime.setVisibility(View.VISIBLE);
                            spinnerFriDutyPost.setVisibility(View.VISIBLE);
                            buttonSaveFri.setEnabled(true);

                            buttonSaveFri.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String dutyTime = spinnerFriDutyTime.getSelectedItem().toString();
                                    String dutyPost = spinnerFriDutyPost.getSelectedItem().toString();
                                    final String dutyPostAndTime = dutyPost + " " + dutyTime;
                                    duty.child(officerId).child("Friday").setValue(dutyPostAndTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                duty.child(officerId).child("officerName").setValue(officerName);
                                                buttonSaveFri.setEnabled(false);
                                                Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });

                        }
                    }
                });

                ssds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String status = ssds.getSelectedItem().toString();

                        spinnerSatDutyPost = findViewById(R.id.ssdp);
                        spinnerSatDutyTime = findViewById(R.id.ssdt);
                        buttonSaveSat = findViewById(R.id.buttonSaveSaturday);


                        if (status.equals("Off Duty")){
                            spinnerSatDutyTime.setVisibility(View.INVISIBLE);
                            spinnerSatDutyPost.setVisibility(View.INVISIBLE);

                            //save off duty status for monday
                            buttonSaveSat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    duty.child(officerId).child("Saturday").setValue("OFF DAY").addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        duty.child(officerId).child("officerName").setValue(officerName);
                                                        buttonSaveSat.setEnabled(false);
                                                        Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                                }
                            });


                        }else {
                            spinnerSatDutyPost.setVisibility(View.VISIBLE);
                            spinnerSatDutyTime.setVisibility(View.VISIBLE);
                            buttonSaveSat.setEnabled(true);

                            buttonSaveSat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String dutyTime = spinnerSatDutyTime.getSelectedItem().toString();
                                    String dutyPost = spinnerSatDutyPost.getSelectedItem().toString();
                                    final String dutyPostAndTime = dutyPost + " " + dutyTime;
                                    duty.child(officerId).child("Saturday").setValue(dutyPostAndTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                duty.child(officerId).child("officerName").setValue(officerName);
                                                buttonSaveSat.setEnabled(false);
                                                Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                ssuds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String status = ssuds.getSelectedItem().toString();

                        spinnerSunDutyPost = findViewById(R.id.susdp);
                        spinnerSunDutyTime = findViewById(R.id.susdt);
                        buttonSaveSun = findViewById(R.id.buttonSaveSunday);


                        if (status.equals("Off Duty")){
                            spinnerSunDutyTime.setVisibility(View.INVISIBLE);
                            spinnerSunDutyPost.setVisibility(View.INVISIBLE);

                            //save off duty status for monday
                            buttonSaveSun.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    duty.child(officerId).child("Sunday").setValue("OFF DAY").addOnCompleteListener(
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        duty.child(officerId).child("officerName").setValue(officerName);
                                                        buttonSaveSun.setEnabled(false);
                                                        Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                    );
                                }
                            });


                        }else {
                            spinnerSunDutyPost.setVisibility(View.VISIBLE);
                            spinnerSunDutyTime.setVisibility(View.VISIBLE);
                            buttonSaveSun.setEnabled(true);

                            buttonSaveSun.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String dutyTime = spinnerSunDutyTime.getSelectedItem().toString();
                                    String dutyPost = spinnerSunDutyPost.getSelectedItem().toString();
                                    final String dutyPostAndTime = dutyPost + " " + dutyTime;
                                    duty.child(officerId).child("Sunday").setValue(dutyPostAndTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                duty.child(officerId).child("officerName").setValue(officerName);
                                                buttonSaveSun.setEnabled(false);
                                                Toast.makeText(AssignDutyActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

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
