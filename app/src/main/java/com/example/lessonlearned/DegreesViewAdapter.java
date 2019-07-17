package com.example.lessonlearned;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lessonlearned.Models.Degree;

import java.util.List;

public class DegreesViewAdapter extends RecyclerView.Adapter<DegreesViewAdapter.ViewHolder> {

    private List<Degree> degrees;
    private LayoutInflater mInflater;
    private ItemClickListener degreeClickListener;

    // data is passed into the constructor
    DegreesViewAdapter(Context context, List<Degree> data) {
        this.mInflater = LayoutInflater.from(context);
        this.degrees = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.degrees_adapter_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String degreeName = degrees.get(position).getName();
        holder.myTextView.setText(degreeName);
    }

    @Override
    public int getItemCount() {
        return degrees.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.degree);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (degreeClickListener != null) degreeClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Degree getItem(int id) {
        return degrees.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.degreeClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
