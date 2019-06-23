package com.example.lessonlearned;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TutorViewAdapter extends RecyclerView.Adapter<TutorViewAdapter.ViewHolder>  {
    private List<Tutor> tutors;
    private LayoutInflater mInflater;
    private TutorViewAdapter.ItemClickListener tutorlickListener;

    // data is passed into the constructor
    TutorViewAdapter(Context context, List<Tutor> data) {
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
        Tutor tutor = tutors.get(position);
        holder.tutorName.setText(tutor.name);
        holder.tutorPrice.setText("$" + tutor.price + "/hour");
        holder.tutorDistance.setText(tutor.distance + " km");

        String courses = tutor.courses.toString().replaceAll("[ \\[ \\] ]", " ").trim();
        int maxChars = 30;

        if (courses.length() > maxChars) courses = courses.substring(0, maxChars - 3).concat(" ...");
        holder.tutorCourses.setText(courses);
    }

    @Override
    public int getItemCount() {
        return tutors.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tutorName, tutorPrice, tutorDistance, tutorCourses;

        ViewHolder(View itemView) {
            super(itemView);
            tutorName = itemView.findViewById(R.id.tutorName);
            tutorPrice = itemView.findViewById(R.id.tutorPrice);
            tutorDistance = itemView.findViewById(R.id.tutorDistance);
            tutorCourses = itemView.findViewById(R.id.tutorCourses);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (tutorlickListener != null) tutorlickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Tutor getItem(int id) {
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
