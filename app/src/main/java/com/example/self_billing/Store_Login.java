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

public class Store_Login extends AppCompatActivity {

    private EditText email, password,store_name;
    TextView register;
    private ProgressBar progressBar;
    Button login;

    private FirebaseAuth mAuth;
//    TextView register=findViewById(R.id.register);

    RelativeLayout relativeLayout1;
    AnimationDrawable animationDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store__login);

        relativeLayout1=(RelativeLayout)findViewById(R.id.relativeLayout);
        animationDrawable=(AnimationDrawable)relativeLayout1.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        store_name=findViewById(R.id.store_name);



        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, Add_Item.class));
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Store_Login.this,Store_Register.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = findViewById(R.id.email);
                password = findViewById(R.id.password);
                progressBar.setVisibility(View.VISIBLE);

                String emailstr=email.getText().toString();
                final String passwordstr = password.getText().toString();

                if(TextUtils.isEmpty(emailstr)){
                    Toast.makeText(Store_Login.this, "Enter Email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(passwordstr)){
                    Toast.makeText(Store_Login.this, "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }



                //authenticate user
                //String namestr=name.getText().toString();
                /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Store_Details_Classs");
                databaseReference.push().setValue(emailstr);*/

                mAuth.signInWithEmailAndPassword(emailstr,passwordstr).addOnCompleteListener(Store_Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            //if there is error
                            if(password.length() < 6){
                                password.setError("Password too short, enter minimum 6 characters!");
                            } else {
                                Toast.makeText(Store_Login.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String store_name_str=store_name.getText().toString();
                            Intent intent = new Intent(Store_Login.this, Add_Item.class);
                            intent.putExtra("Store Name",store_name_str);
                            startActivity(intent);

                        }
                    }
                });


            }
        });
    }
}
