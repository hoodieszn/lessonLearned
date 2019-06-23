package com.example.lessonlearned;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TutorViewAdapter extends RecyclerView.Adapter<TutorViewAdapter.ViewHolder>  {
    private List<TutorsActivity.User> tutors;
    private LayoutInflater mInflater;
    private TutorViewAdapter.ItemClickListener tutorlickListener;

    // data is passed into the constructor
    TutorViewAdapter(Context context, List<TutorsActivity.User> data) {
        this.mInflater = LayoutInflater.from(context);
        this.tutors = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public TutorViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.tutorview_row, parent, false);
        return new TutorViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TutorViewAdapter.ViewHolder holder, int position) {
        TutorsActivity.User tutor = tutors.get(position);
        holder.myTextView.setText(tutor.name + "\n" + tutor.courses.toString().replaceAll("[ \\[ \\] ]", "") + "\n$" + tutor.price + " /hour");
    }

    @Override
    public int getItemCount() {
        return tutors.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tutorName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (tutorlickListener != null) tutorlickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    TutorsActivity.User getItem(int id) {
        return tutors.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(TutorViewAdapter.ItemClickListener itemClickListener) {
        this.tutorlickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
