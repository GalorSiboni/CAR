package com.example.car;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car.Model.Accident;
import com.google.gson.Gson;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Accident> accidentList;
    private ItemClickListener mClickListener;
    private LayoutInflater mInflater;
    private  MySharedPreferences pref;

    public RecyclerViewAdapter(List<Accident> accidentList, Context mContext) {
        this.accidentList = accidentList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_listitem,parent,false);
        pref =  new MySharedPreferences(parent.getContext());
        return new ViewHolder(view);
    }

    //binds the data in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Accident accident = accidentList.get(position);
        String json1 = new Gson().toJson(accident);

        pref.putString(Constants.KEY_SHARED_FREF_EXIST_ACCIDENT, json1);
        holder.accidentDate.setText(String.format("Accident date:\n %s", accident.getOpenDate()));
        holder.accidentLocation.setText(String.format("Accident location:\n %s", accident.getLocationStr()));
        holder.otherDriverInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PopWindowUserInfo.class);
                intent.putExtra(Constants.INTENT_IS_NEW_ACCIDENT, false);
                v.getContext().startActivity(intent);
                // TODO: 17/03/2020 need to pass in json the current profile
            }
        });
    }

    @Override
    public int getItemCount() {
        return accidentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView accidentDate;
        TextView accidentLocation;
        Button otherDriverInfo;
        Button accidentPhotos;
//        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            accidentDate = itemView.findViewById(R.id.accidentDate);
            accidentLocation = itemView.findViewById(R.id.accidentLocation);
            otherDriverInfo = itemView.findViewById(R.id.moreInfo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                mClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    
}
