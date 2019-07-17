package com.example.lessonlearned;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.lessonlearned.Models.ContactedTutor;

import java.util.List;

public class ContactedTutorsViewAdapter extends RecyclerView.Adapter<ContactedTutorsViewAdapter.ViewHolder>{
    private List<ContactedTutor> contactedTutors;
    private LayoutInflater mInflater;
    private ContactedTutorsViewAdapter.ItemClickListener contactedTutorClickListener;

    // data is passed into the constructor
    ContactedTutorsViewAdapter(Context context, List<ContactedTutor> data) {
        this.mInflater = LayoutInflater.from(context);
        this.contactedTutors = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ContactedTutorsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.contacted_tutors_adapter_row, parent, false);
        return new ContactedTutorsViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactedTutorsViewAdapter.ViewHolder holder, int position) {
        ContactedTutor contactedTutor = contactedTutors.get(position);

        // Populate card with posting and tutor information
        if (holder != null) {
            holder.tutorName.setText(contactedTutor.getTutorName());
            holder.tutorPhone.setText(contactedTutor.getTutorPhone());
        }
    }

    @Override
    public int getItemCount() {
        return contactedTutors.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tutorName, tutorPhone;
        Button abuseButton, reviewButton;

        ViewHolder(View itemView) {
            super(itemView);
            tutorName = itemView.findViewById(R.id.contactedTutorName);
            tutorPhone = itemView.findViewById(R.id.contactedTutorPhone);

            abuseButton = itemView.findViewById(R.id.abuseBtn);
            reviewButton = itemView.findViewById(R.id.reviewBtn);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (contactedTutorClickListener != null) contactedTutorClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    ContactedTutor getItem(int id) {
        return contactedTutors.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ContactedTutorsViewAdapter.ItemClickListener itemClickListener) {
        this.contactedTutorClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
