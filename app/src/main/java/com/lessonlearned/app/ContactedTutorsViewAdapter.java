package com.lessonlearned.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.lessonlearned.app.Models.ContactedTutor;
import java.util.List;

public class ContactedTutorsViewAdapter extends RecyclerView.Adapter<ContactedTutorsViewAdapter.ViewHolder> {
    private List<ContactedTutor> contactedTutors;
    private LayoutInflater mInflater;
    private StudentProfileActivity parentContext;

    // data is passed into the constructor
    ContactedTutorsViewAdapter(StudentProfileActivity context, List<ContactedTutor> data) {
        this.parentContext = context;
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
        final ContactedTutor contactedTutor = contactedTutors.get(position);

        // Populate card with posting and tutor information
        if (holder != null) {
            holder.tutorName.setText(contactedTutor.getTutorName());
            holder.tutorPhone.setText(contactedTutor.getTutorPhone());

            if (contactedTutor.isReported()) {
                holder.abuseButton.setVisibility(View.INVISIBLE);
                holder.reviewButton.setVisibility(View.INVISIBLE);

                holder.reportStatic.setVisibility(View.INVISIBLE);
                holder.reviewStatic.setVisibility(View.INVISIBLE);

                holder.reportedText.setVisibility(View.VISIBLE);
            }

            holder.abuseButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    parentContext.showReportAbuseDialog(contactedTutor.getTutorId());
                }
            });

            holder.reviewButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    parentContext.showReviewDialog(contactedTutor.getTutorId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return contactedTutors.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tutorName, tutorPhone, reportedText, reportStatic, reviewStatic;
        ImageButton abuseButton, reviewButton;

        ViewHolder(View itemView) {
            super(itemView);
            tutorName = itemView.findViewById(R.id.contactedTutorName);
            tutorPhone = itemView.findViewById(R.id.contactedTutorPhone);

            reportStatic = itemView.findViewById(R.id.reportstatic);
            reviewStatic = itemView.findViewById(R.id.reviewstatic);

            abuseButton = itemView.findViewById(R.id.abuseBtn);
            reviewButton = itemView.findViewById(R.id.reviewBtn);
            reportedText = itemView.findViewById(R.id.reportedText);
        }
    }

    // convenience method for getting data at click position
    ContactedTutor getItem(int id) {
        return contactedTutors.get(id);
    }
}
