package com.example.semesterprojectv_0_7;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.MyViewHolder> {
    @NonNull
    LayoutInflater inflater;
    private ArrayList<Data> datalar;
    private OnItemClickListener listener;

    public  myAdapter(Context context,ArrayList<Data> datalar,OnItemClickListener listener){
        this.datalar=datalar;
        inflater = LayoutInflater.from(context);
        this.listener=listener;
    }
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.example, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull myAdapter.MyViewHolder myViewHolder, int i) {
        Data selectedProduct = datalar.get(i);
        myViewHolder.setData(selectedProduct, i);
        myViewHolder.bind(datalar.get(i),listener);
    }

    @Override
    public int getItemCount() {
        return datalar.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView notAdi;
        ImageView deleteproduct;
        public MyViewHolder(View itemView) {
            super(itemView);
            notAdi = (TextView) itemView.findViewById(R.id.notAdi);
            deleteproduct = (ImageView) itemView.findViewById(R.id.infoID);
            deleteproduct.setOnClickListener(this);
        }

        public void setData(Data selectedProduct, int position) {

            this.notAdi.setText(selectedProduct.getBaslik());

        }


        @Override
        public void onClick(View v) {


        }


        public void bind(final Data data, final OnItemClickListener listener) {
            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick( data );
                }
            } );
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Data item);
    }
}
