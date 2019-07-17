package com.example.lessonlearned;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.TutorPosting;

import java.util.List;

public class TutorsViewAdapter extends RecyclerView.Adapter<TutorsViewAdapter.ViewHolder>  {
    private List<TutorPosting> postings;
    private LayoutInflater mInflater;
    private TutorsViewAdapter.ItemClickListener tutorlickListener;

    // data is passed into the constructor
    TutorsViewAdapter(Context context, List<TutorPosting> data) {
        this.mInflater = LayoutInflater.from(context);
        this.postings = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public TutorsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.tutorview_row, parent, false);
        return new TutorsViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TutorsViewAdapter.ViewHolder holder, int position) {
        TutorPosting posting = postings.get(position);

        // Populate card with posting and tutor information
        if (holder != null) {
            holder.tutorName.setText(posting.getTutor().getName());
            holder.tutorPrice.setText("$" + Double.toString(posting.getPrice()) + "/hour");
            holder.tutorDistance.setText("0.5 km");

            String courses = "";
            int maxChars = 30;

            for (Course c : posting.getPostingCourses()) {
                courses += c.getName() + "   ";
            }

            if (courses.length() > maxChars)
                courses = courses.substring(0, maxChars - 3).concat(" ...");
            holder.tutorCourses.setText(courses);
        }
    }

    @Override
    public int getItemCount() {
        return postings.size();
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
    TutorPosting getItem(int id) {
        return postings.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(TutorsViewAdapter.ItemClickListener itemClickListener) {
        this.tutorlickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
