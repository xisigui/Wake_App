package com.bscs501.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail, editTextFirstname, editTextLastname, editTextmiddlename, editTextPassword, editTextPasswordMatch, editTextAge;
    private TextView banner;
    private Button registrationbtn;
    private TextView sign;
    private CheckBox checkBox;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

        registrationbtn = (Button) findViewById(R.id.registrationbtn);
        banner = (TextView) findViewById(R.id.banner);
        checkBox = findViewById(R.id.checkBox);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextFirstname = (EditText) findViewById(R.id.editTextFirstname);
        editTextmiddlename = (EditText) findViewById(R.id.editTextMiddlename);
        editTextLastname = (EditText) findViewById(R.id.editTextLastname);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPasswordMatch = (EditText) findViewById(R.id.editTextPasswordMatch);
        sign = findViewById(R.id.signin);

        registrationbtn.setAlpha(.5f);
        registrationbtn.setEnabled(false);

        checkBox.setOnClickListener(this);
        banner.setOnClickListener(this);
        registrationbtn.setOnClickListener(this);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, MainActivity.class));
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registrationbtn:
                registerUser();
                break;
            case R.id.checkBox:
                agree();
                break;
        }
    }

    private void agree() {
        if (checkBox.isChecked()) {
            registrationbtn.setAlpha(1f);
            registrationbtn.setEnabled(true);
        } else {
            registrationbtn.setAlpha(.5f);
            registrationbtn.setEnabled(false);
        }
    }

    private void registerUser() {

        String email = editTextEmail.getText().toString().trim();
        String firstname = editTextFirstname.getText().toString().trim();
        String middlename = editTextmiddlename.getText().toString().trim();
        String lastname = editTextLastname.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordmatched = editTextPasswordMatch.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (firstname.isEmpty()) {
            editTextFirstname.setError("First Name is required");
            editTextFirstname.requestFocus();
            return;
        }

        if (middlename.isEmpty()) {
            editTextmiddlename.setError("Middle Name is required");
            editTextmiddlename.requestFocus();
            return;
        }

        if (lastname.isEmpty()) {
            editTextLastname.setError("Last Name is required");
            editTextLastname.requestFocus();
            return;
        }

        if (age.isEmpty()) {
            editTextAge.setError("Age is required");
            editTextAge.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Middle Name is required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("password length is at least 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        if (!passwordmatched.equals(password)) {
            editTextPasswordMatch.setError("Password does not match");
            editTextPasswordMatch.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(firstname, middlename, lastname, email, age);
                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(Registration.this, "User has been registered", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Registration.this, MainActivity.class));

                                    } else {
                                        Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}