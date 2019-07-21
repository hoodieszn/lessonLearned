package com.example.lessonlearned;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.TutorPosting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TutorsViewAdapter extends RecyclerView.Adapter<TutorsViewAdapter.ViewHolder> implements Filterable {
    private List<TutorPosting> postings;
    private List<TutorPosting> filteredPostings;
    private LayoutInflater mInflater;
    private TutorsViewAdapter.ItemClickListener tutorlickListener;
    private Filter mFilter;

    // data is passed into the constructor
    TutorsViewAdapter(Context context, List<TutorPosting> data) {
        this.mInflater = LayoutInflater.from(context);
        this.postings = data;
        this.filteredPostings = data;
        this.mFilter = new ItemFilter();
    }

    // inflates the row layout from xml when needed
    @Override
    public TutorsViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.tutors_adapter_row, parent, false);
        return new TutorsViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TutorsViewAdapter.ViewHolder holder, int position) {
        TutorPosting posting = filteredPostings.get(position);

        // Populate card with posting and tutor information
        if (holder != null) {
            holder.tutorName.setText(posting.getTutorName());

            holder.tutorPrice.setText("$" + Double.toString(posting.getPrice()) + "/hour");

            DecimalFormat df = new DecimalFormat("#.#");

            double avgRating = posting.getRating();
            if (avgRating == 0){
                holder.tutorRating.setText("No Rating Yet");
                holder.imageStar.setVisibility(View.GONE);
            }
            else {
                holder.tutorRating.setText(df.format((avgRating)));
            }

            holder.tutorDistance.setText(df.format(posting.getDistance()) + " km");

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
        return filteredPostings.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    // convenience method for getting data at click position
    TutorPosting getItem(int id) {
        return filteredPostings.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(TutorsViewAdapter.ItemClickListener itemClickListener) {
        this.tutorlickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = postings;
                results.count = postings.size();
                return results;
            }

            String filterString = constraint.toString().toLowerCase();


            final List<TutorPosting> list = postings;

            int count = list.size();
            final ArrayList<TutorPosting> nlist = new ArrayList<TutorPosting>(count);

            TutorPosting filterablePosting ;

            for (int i = 0; i < count; i++) {
                filterablePosting = list.get(i);
                if (filterablePosting.containsCourse(filterString)) {
                    nlist.add(filterablePosting);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredPostings = (ArrayList<TutorPosting>) results.values;
            notifyDataSetChanged();
        }

    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tutorName, tutorPrice, tutorDistance, tutorCourses, tutorRating;
        ImageView imageStar;

        ViewHolder(View itemView) {
            super(itemView);
            tutorName = itemView.findViewById(R.id.tutorName);
            tutorPrice = itemView.findViewById(R.id.tutorPrice);
            tutorDistance = itemView.findViewById(R.id.tutorDistance);
            tutorCourses = itemView.findViewById(R.id.tutorCourses);
            tutorRating  = itemView.findViewById(R.id.ratingText);
            imageStar = itemView.findViewById(R.id.imageStar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (tutorlickListener != null) tutorlickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
