package com.bscs501.prototype;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marozzi.roundbutton.RoundButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView registraiton, forgotpassword, showbtn, k;
    private EditText editTextEmail, editTextPassword;
    private RoundButton loginbtn;
    private FirebaseAuth mAuth;
    private View background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout constraintLayout = findViewById(R.id.Main);
        AnimationDrawable animationDrawable =(AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        background = findViewById(R.id.over);
        TextView wake = findViewById(R.id.textView);
        k = findViewById(R.id.textView2);
        loginbtn = (RoundButton) findViewById(R.id.loginbtn);
        registraiton = (TextView) findViewById(R.id.registrationbtn);
        showbtn = (TextView) findViewById(R.id.showbtn);
        editTextEmail = (EditText) findViewById(R.id.status);
        editTextPassword = (EditText) findViewById(R.id.password);

        registraiton.setOnClickListener(this);
        loginbtn.setOnClickListener(this);
        k.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, activity_callerpick.class));
            }
        });

        showbtn.setVisibility(View.GONE);
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextPassword.getText().length() > 0) {
                    showbtn.setVisibility(View.VISIBLE);
                } else {
                    showbtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showbtn.getText() == "Show") {
                    showbtn.setText("Hide");
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    editTextPassword.setSelection(editTextPassword.length());
                } else {
                    showbtn.setText("Show");
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editTextPassword.setSelection(editTextPassword.length());
                }
            }
        });

        forgotpassword = (TextView) findViewById(R.id.forgotpassword);
        forgotpassword.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registrationbtn:
                startActivity(new Intent(this, Registration.class));
                break;

            case R.id.loginbtn:
                userlogin();
                break;

            case R.id.forgotpassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void userlogin() {
        loginbtn.startAnimation();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is Empty");
            editTextEmail.requestFocus();
            loginbtn.revertAnimation();
            editTextEmail.startAnimation(shakeError());
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please Enter a Valid Email");
            editTextEmail.requestFocus();
            loginbtn.revertAnimation();
            editTextEmail.startAnimation(shakeError());
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is empty");
            editTextPassword.requestFocus();
            loginbtn.revertAnimation();
            editTextPassword.startAnimation(shakeError());
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Password Length is at Least 6 Characters.");
            editTextPassword.requestFocus();
            loginbtn.revertAnimation();
            editTextPassword.startAnimation(shakeError());
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()) {
                        editTextEmail.clearComposingText();
                        editTextPassword.clearComposingText();
                        circularRevealActivity();
                        startActivity(new Intent(MainActivity.this, Dashboard.class));
                        finish();
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Please Check your Email to Verify your Account.", Toast.LENGTH_LONG).show();
                        loginbtn.revertAnimation();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Login Credentials in Valid, Please try again.", Toast.LENGTH_LONG).show();
                    loginbtn.revertAnimation();
                }
            }
        });
    }

    private void circularRevealActivity() {
        int cx = (background.getLeft() + background.getRight()) / 2;
        int cy = (background.getTop() + background.getBottom()) / 2;
        float finalRadius = Math.max(background.getWidth(), background.getHeight());

        Animator circularReveal = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            circularReveal = ViewAnimationUtils.createCircularReveal(
                    background, cx, cy, 0, finalRadius);
        }
        circularReveal.setDuration(300);
        background.setVisibility(View.VISIBLE);
        loginbtn.setVisibility(View.GONE);
        circularReveal.start();
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 15, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(3));
        return shake;
    }

}