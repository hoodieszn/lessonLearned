package com.example.lessonlearned;

import android.content.Intent;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.TutorPosting;
import com.google.android.flexbox.FlexboxLayout;
import java.util.ArrayList;
import java.util.List;

public class ActivePostsViewAdapter extends RecyclerView.Adapter<ActivePostsViewAdapter.ViewHolder> {
    private List<TutorPosting> activePostings = new ArrayList<>();
    private Context mContext;

    public ActivePostsViewAdapter(List<TutorPosting> activePostings, Context mContext) {
        this.activePostings = activePostings;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_posting_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final TutorPosting posting = activePostings.get(position);

        viewHolder.name.setText(posting.getTutorName());
        viewHolder.price.setText("$" + Double.toString(posting.getPrice()) + "/hour");

        String courses = "";
        int maxChars = 30;

        for (Course c : posting.getPostingCourses()) {
            courses += c.getName() + "   ";
        }

        if (courses.length() > maxChars)
            courses = courses.substring(0, maxChars - 3).concat(" ...");
        viewHolder.courses.setText(courses);

        final Intent tutorPosting = new Intent(mContext, TutorPostingActivity.class);
        tutorPosting.putExtra("tutorId", posting.getTutorId());
        tutorPosting.putExtra("postingId", posting.getId());

        viewHolder.activePostingLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(tutorPosting);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activePostings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        FlexboxLayout activePostingLayout;
        TextView name;
        TextView courses;
        ImageButton deleteButton;
        TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            activePostingLayout = itemView.findViewById(R.id.activePostingLayout);
            name = itemView.findViewById(R.id.name);
            courses = itemView.findViewById(R.id.courses);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            price = itemView.findViewById(R.id.price);
        }
    }
}
