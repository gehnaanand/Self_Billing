package com.example.self_billing;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Billing extends AppCompatActivity {

    private Button Scan,GenerateBill;
    private ListView listView;
    private String scannedTextResult;
    ArrayList<Ordered_Item_Class> order;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        Scan = findViewById(R.id.btnScan);
        GenerateBill = findViewById(R.id.btnPayment);
        listView = findViewById(R.id.listview);

        Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Billing.this,Scanner.class);
                startActivityForResult(intent,1);
            }
        });

        Intent intent = getIntent();
        String store_name = intent.getStringExtra("Store Name");



    }
    public int calculateOrderTotal(){
        int orderTotal = 0;
        for(Ordered_Item_Class o : order){
            orderTotal+= o.getCost()*o.getQuantity();
        }
        return orderTotal;
    }
    DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            total.setText("Total = "+ calculateOrderTotal());
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                scannedTextResult = data.getStringExtra("Result");
                FirebaseDatabase database;
                DatabaseReference ref;
                database=FirebaseDatabase.getInstance();
                ref=database.getReference("Store_Items/Store 1");

                /*final ArrayList<String> ItemNameList = new ArrayList<>();
                final ArrayList<Integer> QuantityList = new ArrayList<>();
                final  ArrayList<Integer> CostList = new ArrayList<>();*/

                total = findViewById(R.id.total);
                final MyListAdapter adapter = new MyListAdapter(Billing.this,R.layout.custom_listview,order);
                //final ListAdapter adapter=new ArrayAdapter<String>(Stores_Available.this,android.R.layout.simple_list_item_1,list);

                ref.addValueEventListener(new ValueEventListener() {
                    Item_Details_Class Item = new Item_Details_Class();

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds :dataSnapshot.getChildren()){
                            Item = ds.getValue(Item_Details_Class.class);
                            if(scannedTextResult.equals(Item.getBarcodeID())) {
                                String name = Item.getName();
                                int cost = Item.getCost();
                                Ordered_Item_Class orders = new Ordered_Item_Class(name,cost);
                                order.add(orders);
                                /*ItemNameList.add(" " + Item.getName());
                                QuantityList.add(1);
                                CostList.add(Item.getCost());*/
                                break;
                            }
                            else{
                                continue;
                            }
                            //listView.setAdapter(adapter)
                        }
                        listView.setAdapter(adapter);
                        //adapter.registerDataSetObserver(observer);
                        if(order.isEmpty()){
                            Toast.makeText(Billing.this, "Nothing", Toast.LENGTH_SHORT).show();
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }
    }

    private class MyListAdapter extends ArrayAdapter<Ordered_Item_Class> {
        private List<Ordered_Item_Class> itemNameList;
        private int layout;
        private Context context;
        private TextView currentItemName,currentCost,currentQuantity;
        private Button add,minus;


        public MyListAdapter(@NonNull Context context, int resource,List<Ordered_Item_Class> myOrders ) {
            super(context, resource,myOrders);
            layout=resource;
            this.itemNameList = myOrders;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //View listItemView = convertView;
            //if(listItemView == null){
              //  listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_listview,parent,false);
            //}

            ViewHolder mainViewHolder = null;

            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();

                final Ordered_Item_Class currentOrder = getItem(position);
                viewHolder.currentItemName = (TextView)listItemView.findViewById(R.id.tvItemName);
                currentCost = (TextView)listItemView.findViewById(R.id.tvCost);
                currentQuantity = (TextView)listItemView.findViewById(R.id.tvQuantity);
                add = (Button)listItemView.findViewById(R.id.btnPlus);
                minus = (Button)listItemView.findViewById(R.id.btnMinus);
            }


            currentItemName.setText(currentOrder.getName());
            currentCost.setText(currentOrder.getCost()*currentOrder.getQuantity());
            currentQuantity.setText(currentOrder.getQuantity());

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentOrder.addQuantity();
                    currentQuantity.setText(currentOrder.getQuantity());
                    currentCost.setText(currentOrder.getCost()*currentOrder.getQuantity());
                    notifyDataSetChanged();
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentOrder.removeQuantity();
                    currentQuantity.setText(currentOrder.getQuantity());
                    currentCost.setText(currentOrder.getCost()*currentOrder.getQuantity());
                    notifyDataSetChanged();
                }
            });

            return listItemView;



            /*ViewHolder mainViewHolder = null;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout,parent,false);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.tvItemName = convertView.findViewById(R.id.tvItemName);
                viewHolder.plusButton = convertView.findViewById(R.id.btnPlus);
                viewHolder.minusButton = convertView.findViewById(R.id.btnMinus);
                viewHolder.tvQuantity = convertView.findViewById(R.id.tvQuantity);
                viewHolder.tvCost = convertView.findViewById(R.id.tvCost);

                viewHolder.tvItemName.setText(getItem(position));
                viewHolder.tvCost.setText(getItem(position));
                viewHolder.tvQuantity.setText(getItem(position));
                viewHolder.plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Intent intent=new Intent(Stores_Available.this,MapsActivity.class);
                        //String store_name=(String)getItem(position);
                        //Toast.makeText(Billing.this, ""+getItem(position), Toast.LENGTH_SHORT).show();
                        //intent.putExtra("Store Name",store_name);
                        //go to maps activity
                        //startActivity(intent);
                        Integer qty = Integer.parseInt(viewHolder.tvQuantity.getText().toString());
                        qty++;
                        viewHolder.tvQuantity.setText(qty.toString());
                    }
                });

                viewHolder.minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer qty = Integer.parseInt(viewHolder.tvQuantity.getText().toString());
                        if(qty!= 0)
                            qty--;
                        viewHolder.tvQuantity.setText(qty.toString());
                    }
                });
                convertView.setTag(viewHolder);
                Toast.makeText(Billing.this, "Helloooooooo", Toast.LENGTH_SHORT).show();
            }
            else {
                mainViewHolder = (ViewHolder)convertView.getTag();
                mainViewHolder.tvItemName.setText(getItem(position));
                mainViewHolder.tvQuantity.setText(getItem(position));
                mainViewHolder.tvCost.setText(getItem(position));
                Toast.makeText(Billing.this, "World", Toast.LENGTH_SHORT).show();
            }
            return convertView;
            //return super.getView(position, convertView, parent);*/
        }
    }
    /*public class ViewHolder{

        TextView tvItemName, tvQuantity,tvCost;
        Button minusButton,plusButton;
    }*/
}
