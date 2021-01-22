package com.bscs501.prototype;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    private Button logout;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        navigation = findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.pro);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dash:
                        startActivity(new Intent(Dashboard.this, activity_callerpick.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.pro:
                        overridePendingTransition(0,0);
                        break;
                }
                return true;
            }
        });

        logout = (Button) findViewById(R.id.logoutbtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Dashboard.this, MainActivity.class));
            }
        });

        ConstraintLayout LL = findViewById(R.id.Dashboard);
        AnimationDrawable AD = (AnimationDrawable) LL.getBackground();
        AD.setEnterFadeDuration(2000);
        AD.setExitFadeDuration(4000);
        AD.start();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User");
        userID = user.getUid();

        final TextView fullnameTextView = (TextView)findViewById(R.id.fullnameTextview);
        final TextView emailTextView = (TextView)findViewById(R.id.emailTextview);
        final TextView ageTextView = (TextView)findViewById(R.id.ageTextview);

        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String firstname = userProfile.firstname;
                    String middlename = userProfile.middlename;
                    String lastname = userProfile.lastname;
                    String email = userProfile.email;
                    String age = userProfile.age;

                    fullnameTextView.setText(firstname + " " + middlename + " " + lastname);
                    emailTextView.setText(email);
                    ageTextView.setText(age);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }
}