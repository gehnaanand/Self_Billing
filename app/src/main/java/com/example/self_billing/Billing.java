package com.example.self_billing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Billing extends AppCompatActivity {

    private Button Scan,Payment;
    private ListView listView;
    private String scannedTextResult,store_name;
    private TextView tvTotal;
    private int GOOGLE_PAY_REQUEST_CODE;
    private Integer total = 0;

    final ArrayList<String> ItemNameList = new ArrayList<>();
    final ArrayList<Integer> QuantityList = new ArrayList<>();
    final  ArrayList<Integer> CostList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        Scan = findViewById(R.id.btnScan);
        Payment = findViewById(R.id.btnPayment);
        listView = findViewById(R.id.listview);

        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Billing.this,Scanner.class);
                startActivityForResult(intent,1);
            }
        });

        Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
                GOOGLE_PAY_REQUEST_CODE = 123;

                Uri uri =
                        new Uri.Builder()
                                .scheme("upi")
                                .authority("pay")
                                .appendQueryParameter("pa", "test@axisbank")
                                .appendQueryParameter("pn", "Test Merchant")
                                .appendQueryParameter("mc", "1234")
                                .appendQueryParameter("tr", "123456789")
                                .appendQueryParameter("tn", "test transaction note")
                                .appendQueryParameter("am", total.toString() )
                                .appendQueryParameter("cu", "INR")
                                .appendQueryParameter("url", "https://test.merchant.website")
                                .build();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
                startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
            }
        });


        Intent intent = getIntent();
        store_name = intent.getStringExtra("Store Name");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                scannedTextResult = data.getStringExtra("Result");
                FirebaseDatabase database;
                DatabaseReference ref;
                database=FirebaseDatabase.getInstance();
                ref=database.getReference("Store_Items/"+store_name);

                final MyListAdapter adapter = new MyListAdapter(Billing.this,R.layout.custom_listview,ItemNameList,QuantityList,CostList);

                ref.addValueEventListener(new ValueEventListener() {
                    Item_Details_Class Item = new Item_Details_Class();

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds :dataSnapshot.getChildren()){
                            Item = ds.getValue(Item_Details_Class.class);

                            if(scannedTextResult.equals(Item.getBarcodeID())) {
                                ItemNameList.add(" " + Item.getName());
                                QuantityList.add(1);
                                CostList.add(Item.getCost());
                                int total = 0;
                                for(int i=0;i<CostList.size();i++)
                                    total +=CostList.get(i)*QuantityList.get(i);
                                tvTotal = findViewById(R.id.tvTotal);
                                tvTotal.setText("Total = " + total);
                                break;
                            }
                            else{
                                continue;
                            }
                            //listView.setAdapter(adapter)
                        }
                        listView.setAdapter(adapter);
                        if(ItemNameList.isEmpty()){
                            ItemNameList.add("Nothing");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }
        else if(requestCode == GOOGLE_PAY_REQUEST_CODE)
        {
            Log.d("result", data.getStringExtra("Status"));
        }
    }

    private class MyListAdapter extends ArrayAdapter<String> {
        private int layout;
        private ArrayList<Integer> costList,qtyList;
        private  ArrayList<String> ItemNames;
        public MyListAdapter(@NonNull Context context, int resource, ArrayList<String> list, ArrayList<Integer> qty, ArrayList<Integer> cost) {
            super(context, resource,list);
            layout=resource;
            ItemNames = list;
            costList = cost;
            qtyList = qty;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder mainViewHolder = null;

            if(convertView==null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.tvItemName = convertView.findViewById(R.id.tvItemName);
                viewHolder.plusButton = convertView.findViewById(R.id.btnPlus);
                viewHolder.minusButton = convertView.findViewById(R.id.btnMinus);
                viewHolder.tvQuantity = convertView.findViewById(R.id.tvQuantity);
                viewHolder.tvCost = convertView.findViewById(R.id.tvCost);

                viewHolder.tvItemName.setText(getItem(position));
                viewHolder.tvCost.setText((qtyList.get(position)*costList.get(position) )+ " ");
                viewHolder.tvQuantity.setText(qtyList.get(position).toString());

                viewHolder.plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer qty = Integer.parseInt(viewHolder.tvQuantity.getText().toString());
                        qty++;
                        qtyList.set(position,qty);
                        viewHolder.tvQuantity.setText(qty.toString());
                        viewHolder.tvCost.setText((qty*costList.get(position)) + "");
                        total = 0;
                        for(int i=0;i<CostList.size();i++)
                            total +=CostList.get(i)*QuantityList.get(i);
                        tvTotal = findViewById(R.id.tvTotal);
                        tvTotal.setText("Total = " + total);

                    }
                });

                viewHolder.minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer qty = Integer.parseInt(viewHolder.tvQuantity.getText().toString());
                        if(qty==1) {
                            qtyList.remove(position);
                            costList.remove(position);
                            ItemNames.remove(position);
                            notifyDataSetChanged();
                            //listView.removeViewAt(position);
                        }
                        //if(qty!= 1)
                        else {
                            qty--;
                            qtyList.set(position, qty);
                            viewHolder.tvQuantity.setText(qty.toString());
                            viewHolder.tvCost.setText((qty * costList.get(position)) + "");
                        }
                        int total = 0;
                        for(int i=0;i<CostList.size();i++)
                            total +=CostList.get(i)*QuantityList.get(i);
                        tvTotal = findViewById(R.id.tvTotal);
                        tvTotal.setText("Total = " + total);

                    }
                });
                convertView.setTag(viewHolder);
            }
            else {
                mainViewHolder = (ViewHolder)convertView.getTag();
                mainViewHolder.tvItemName.setText(ItemNames.get(position).toString());
                mainViewHolder.tvQuantity.setText(qtyList.get(position).toString());
                mainViewHolder.tvCost.setText((qtyList.get(position)*costList.get(position) )+ " ");
            }
            return convertView;
        }
    }
    public class ViewHolder{

        TextView tvItemName, tvQuantity,tvCost;
        Button minusButton,plusButton;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Billing.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
