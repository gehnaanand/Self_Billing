package com.example.self_billing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Store_grid extends AppCompatActivity{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<String> Stores ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_grid);
        Stores = new ArrayList<String>();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Store_grid.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //Get reference
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Store_Details");

        // Attach a listener to read the data
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("kidi","a;kdvaklkadflkandka");
                Toast.makeText(getApplicationContext(),  "" + dataSnapshot, Toast.LENGTH_LONG).show();

                for (DataSnapshot i: dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot_store: i.getChildren()
                         ) {
                        Store_Class store = snapshot_store.getValue(Store_Class.class);
                        Stores.add(store.getStore_Name());
                        setAdapter();
                        Toast.makeText(getApplicationContext(),  "     Store :"+store.getStore_Name() + "   email: "+ store.getStore_Email(), Toast.LENGTH_LONG).show();

                    }
                }
          }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                Toast.makeText(getApplicationContext(), "On Cancelled ",Toast.LENGTH_LONG).show();

            }
        });

//        Stores.addAll("Store1","Store2","Store3","Store4","Store5","Store6","Store7","Store8","Store9","Store10","Store11","Store12","Store13","Store14");
/*        Stores.add("Store7");
        Stores.add("Store8");
        Stores.add("Store9");
        Stores.add("Store6");
        Stores.add("Store4");
*/
        Toast.makeText(getApplicationContext(),"Final : "+ Stores, Toast.LENGTH_LONG).show();
        setAdapter();
    }

    void setAdapter(){
        RecyclerView myrv = findViewById(R.id.rvStores);
        RecyclerViewAdapter myAdapter = new RecyclerViewAdapter(this, Stores);
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        myrv.setAdapter(myAdapter);
    }
}



