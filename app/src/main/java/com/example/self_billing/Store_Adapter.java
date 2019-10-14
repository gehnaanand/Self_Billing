package com.example.self_billing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class Store_Adapter extends RecyclerView.Adapter<Store_Adapter.MyViewHolder> {
    private String[] mDataset;
    private static RecyclerViewClickListener itemListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView;
        public MyViewHolder(TextView v) {
            super(v);
            textView = v;
            v.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View v) {
//            itemListener.recyclerViewListClicked(v, this.getPosition());
            int pos = getAdapterPosition();

            // check if item still exists
            if(pos != RecyclerView.NO_POSITION){
                String clickedDataItem = mDataset[pos];
                itemListener.recyclerViewListClicked(v, clickedDataItem);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public Store_Adapter(String[] myDataset, RecyclerViewClickListener itemListener) {
        mDataset = myDataset;
        this.itemListener = itemListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Store_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_textview, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
