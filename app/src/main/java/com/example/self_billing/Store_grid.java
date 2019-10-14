package com.example.self_billing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Store_grid extends AppCompatActivity implements RecyclerViewClickListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Store_Adapter mAdapter;
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


        recyclerView =findViewById(R.id.rvStores);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Stores = new String[]{"Store1","Store2","Store3","Store4","Store5","Store6","Store7","Store1","Store2","Store3","Store4","Store5","Store6","Store7"};
        mAdapter = new Store_Adapter(Stores,  this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void recyclerViewListClicked(View v, String StoreName){
        Intent intent = new Intent(Store_grid.this,Add_Item.class);
        intent.putExtra("StoreName",StoreName);
        startActivity(intent);
    }

}
