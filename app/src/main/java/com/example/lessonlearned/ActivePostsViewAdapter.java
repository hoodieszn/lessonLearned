package com.example.lessonlearned;

import android.content.Intent;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.TutorPosting;
import com.example.lessonlearned.Models.UserType;
import com.example.lessonlearned.Services.RESTClient;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ActivePostsViewAdapter extends RecyclerView.Adapter<ActivePostsViewAdapter.ViewHolder> {
    private List<TutorPosting> activePostings = new ArrayList<>();
    private Context mContext;
    TutorPosting posting;

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
                mContext.startActivity(tutorPosting);
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

            deleteButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int postId = posting.getId();
                    try {
                        RESTClientRequest.deletePosting(postId, ActivePostsViewAdapter.this);
                    }
                    catch (JSONException e){
                        Log.d("JSONException", e.toString());
                    }

                }
            });
        }
    }

}
