package com.example.self_billing;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_Item extends AppCompatActivity {

    private TextView scannedText;
    private EditText itemName, cost;
    String strItemName,scannedTextResult;
    int intCost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        ImageButton imageButton = findViewById(R.id.btnScan);
        Button addItem = findViewById(R.id.btnAddItem);
        itemName = findViewById(R.id.etItemName);
        cost = findViewById(R.id.etCost);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_Item.this,Scanner.class);
                startActivityForResult(intent,1);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strItemName = itemName.getText().toString();
                intCost = Integer.parseInt(cost.getText().toString());

                Intent intent = getIntent();
                String store_name_str = intent.getStringExtra("Store Name");

                Item_Details_Class item = new Item_Details_Class(scannedTextResult,strItemName,intCost);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Store_Items");
                DatabaseReference childref = ref.child(store_name_str).push();
                childref.setValue(item);
                itemName.setText("");
                cost.setText("");
                scannedText.setText("Scan Item");
                Toast.makeText(Add_Item.this, "Added Item Successfully!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                scannedTextResult = data.getStringExtra("Result");
                scannedText = findViewById(R.id.tvScannedID);
                scannedText.setText("" + scannedTextResult);
            }
        }
    }
}
