package com.example.self_billing;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button buttonLogin,store_login;
    private TextView textviewRegister, textviewForgot;

    RelativeLayout relativeLayout1;
    AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout1=(RelativeLayout)findViewById(R.id.relativeLayout);
        animationDrawable=(AnimationDrawable)relativeLayout1.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(MainActivity.this, Store_grid.class));

        }

        inputEmail = findViewById(R.id.etEmail);
        inputPassword = findViewById(R.id.etPassword);
        buttonLogin = findViewById(R.id.btnLogin);
        textviewRegister = findViewById(R.id.tvRegister);
        textviewForgot = findViewById(R.id.tvForgot);
        progressBar = findViewById(R.id.progressBar);
        store_login=findViewById(R.id.btnStore_owner);


        //Get Firebase auth instance

        auth = FirebaseAuth.getInstance();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    //Toast.makeText(MainActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                    inputEmail.setError("Enter email address");
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(MainActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
                    inputPassword.setError("Enter the Password");

                    return;
                }
                if (password.length() < 6) {
                    progressBar.setVisibility(View.GONE);
                    inputPassword.setError("Password too short, enter minimum 6 characters!");
                    return;
                }


                //authenticate user

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            //if there is error
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, Store_grid.class);
                            startActivity(intent);

                        }
                    }
                });
            }
        });

        textviewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register_user.class));
            }
        });


    }




}
