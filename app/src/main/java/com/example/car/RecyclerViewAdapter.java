package com.example.car;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.car.Model.Accident;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Accident> accidentList;
    private ItemClickListener mClickListener;
    private LayoutInflater mInflater;

    public RecyclerViewAdapter(List<Accident> accidentList, Context mContext) {
        this.accidentList = accidentList;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_listitem,parent,false);
        return new ViewHolder(view);
    }

    //binds the data in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Accident accident = accidentList.get(position);
//        holder.accidentDate.setText();
//        holder.personName.setText(person.getName());
//        holder.personScore.setText(person.getScore()+"");
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
//            personName = itemView.findViewById(R.id.personName);
//            personScore = itemView.findViewById(R.id.personScore);
//            parentLayout = itemView.findViewById(R.id.parentLayout);
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
