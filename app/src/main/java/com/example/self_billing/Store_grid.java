package com.example.self_billing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class Store_grid extends AppCompatActivity{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String[] Stores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_grid);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Store_grid.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Stores = new String[]{"Store1","Store2","Store3","Store4","Store5","Store6","Store7","Store8","Store9","Store10","Store11","Store12","Store13","Store14"};
        RecyclerView myrv = findViewById(R.id.rvStores);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, Arrays.asList(Stores));
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        myrv.setAdapter(myAdapter);
    }
}



