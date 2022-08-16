package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//---------------------------------------Code Attribution------------------------------------------------
//Author:Ben O'Brien
//Uses:Populate the recycler view with collection items
public class recyclerItemAdapter extends RecyclerView.Adapter<recyclerItemAdapter.MyViewHolder>  {

    //Global Variable
    private ArrayList<Item> tempItemList;
    Context context;
    private OnItemClickListerner mListener;

    //Constructor
    public recyclerItemAdapter(ArrayList<Item> tempItemList, Context context) {
        this.tempItemList = tempItemList;
        this.context = context;
    }

    //Interface of onclicklistener
    public interface OnItemClickListerner {
        void onItemClick(int position);
    }

    //Method set the onclicklistener
    public void setOnItemClickListerner(OnItemClickListerner listener){
        mListener =listener;
    }

    //get the specfic view that must be used to display the data
    @NonNull
    @Override
    public recyclerItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_item,parent,false);
        return new MyViewHolder(itemView,mListener);
    }

    //Display data in the recycler view
    @Override
    public void onBindViewHolder(@NonNull recyclerItemAdapter.MyViewHolder holder, int position) {
        String name = tempItemList.get(position).getItemTitle();
        holder.txt_ItemTitle.setText(name);
        //---------------------------------------Code Attribution------------------------------------------------
        //Author:CodingSTUFF
        //Uses:Display image form firebase storage using url
        Glide.with(context).load(tempItemList.get(position).getImgURL()).into(holder.img_ItemImage);
        //Link:https://www.youtube.com/watch?v=iEcokZOv5UY
        //-----------------------------------------------End------------------------------------------------------
    }

    @Override
    public int getItemCount() {
        return tempItemList.size();
    }

    //Myviewholder class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_ItemTitle;
        private ImageView img_ItemImage;
        OnItemClickListerner OnItemClickListerner;

        public MyViewHolder(final View view, OnItemClickListerner listerner){
            super(view);
            txt_ItemTitle = view.findViewById(R.id.txt_ItemTitle);
            img_ItemImage =view.findViewById(R.id.img_ItemImage);
            this.OnItemClickListerner = listerner;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listerner != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listerner.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
}
//Link:https://www.youtube.com/watch?v=__OMnFR-wZU
//-----------------------------------------------End------------------------------------------------------
