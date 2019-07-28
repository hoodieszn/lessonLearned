package com.lessonlearned.app;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lessonlearned.app.Models.Course;
import com.lessonlearned.app.Models.TutorPosting;
import com.lessonlearned.app.Services.RESTClientRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ActivePostsViewAdapter extends RecyclerView.Adapter<ActivePostsViewAdapter.ViewHolder> {
    private List<TutorPosting> activePostings = new ArrayList<>();
    private TutorProfileActivity mContext;
    TutorPosting posting;

    final static int tutorProfileRequest = 0;

    public ActivePostsViewAdapter(List<TutorPosting> activePostings, TutorProfileActivity mContext) {
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
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        posting = activePostings.get(position);

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
                mContext.dimmer.setVisibility(View.VISIBLE);
                mContext.startActivityForResult(tutorPosting, tutorProfileRequest);
            }
        });

        viewHolder.deleteButton.setOnClickListener( new View.OnClickListener() {
            final int postId = posting.getId();

            @Override
            public void onClick(View view) {
                try {
                    RESTClientRequest.deletePosting(postId, ActivePostsViewAdapter.this);
                }
                catch (JSONException e){
                    // Log.d("JSONException", e.toString());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return activePostings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout activePostingLayout;
        TextView courses;
        ImageButton deleteButton;
        TextView price;

        public ViewHolder(View itemView) {
            super(itemView);

            activePostingLayout = itemView.findViewById(R.id.activePostingLayout);
            courses = itemView.findViewById(R.id.courses);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            price = itemView.findViewById(R.id.price);
        }
    }
}
