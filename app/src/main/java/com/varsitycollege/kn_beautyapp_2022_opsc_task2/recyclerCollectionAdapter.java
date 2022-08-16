package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*import com.google.firebase.database.ValueEventListener;*/

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//---------------------------------------Code Attribution------------------------------------------------
//Author:Ben O'Brien
//Uses:Populate Recycler for collection
public class recyclerCollectionAdapter extends RecyclerView.Adapter<recyclerCollectionAdapter.MyViewHolder> {

    //Global Variable
    private ArrayList<Collection> tempCollectionList;
    Context context;
    private OnCollectionClickListerner mListener;

    //Constructor
    public recyclerCollectionAdapter(ArrayList<Collection> collectList,Context context)
    {
        this.tempCollectionList = collectList;
        this.context =context;
    }

    //Interface of onclicklistener
    public interface OnCollectionClickListerner {
        void onCollectionClick(int position);
    }

    //Method set the onclicklistener
    public void setOnCollectionClickListerner(OnCollectionClickListerner listener){
        mListener =listener;
    }

    //Myviewholder class
    public class MyViewHolder extends RecyclerView.ViewHolder {
        //variables
        private TextView txt_CollectionName;
        private ImageView img_Collection;
        OnCollectionClickListerner OnCollectionClickListerner;

        //myviewholder constructor
        public MyViewHolder(final View view,OnCollectionClickListerner listerner){
            super(view);
            txt_CollectionName = view.findViewById(R.id.txt_CollectionNameDisplay);
            img_Collection =view.findViewById(R.id.img_CollectionImage);
            this.OnCollectionClickListerner = OnCollectionClickListerner;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listerner != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listerner.onCollectionClick(position);
                        }
                    }
                }
            });
        }
    }

    //get the specfic view that must be used to display the data
    @NonNull
    @Override
    public recyclerCollectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_collection,parent,false);
        return new MyViewHolder(itemView,mListener);
    }

    //Display data in the recycler view
    @Override
    public void onBindViewHolder(@NonNull recyclerCollectionAdapter.MyViewHolder holder, int position) {
        String name = tempCollectionList.get(position).getCollectionName();
        holder.txt_CollectionName.setText(name);
        //---------------------------------------Code Attribution------------------------------------------------
        //Author:CodingSTUFF
        //Uses:Display an image with the url from firebase storage
        Glide.with(context).load(tempCollectionList.get(position).getImageURl()).into(holder.img_Collection);
        //Link:https://www.youtube.com/watch?v=iEcokZOv5UY
        //-----------------------------------------------End------------------------------------------------------
    }

    @Override
    public int getItemCount() {
        return tempCollectionList.size();
    }
}
//Link:https://www.youtube.com/watch?v=__OMnFR-wZU
//-----------------------------------------------End------------------------------------------------------
