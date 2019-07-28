package com.lessonlearned.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CommentsViewAdapter extends RecyclerView.Adapter<CommentsViewAdapter.ViewHolder> {

    private List<String> comments = new ArrayList<>();
    private List<String> commentOwners = new ArrayList<>();

    public CommentsViewAdapter(List<String> comments, List<String> commentOwners) {
        this.comments = comments;
        this.commentOwners = commentOwners;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.comment.setText(comments.get(position));
        holder.commentOwner.setText(commentOwners.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
            TextView comment;
            RelativeLayout commentLayout;
            TextView commentOwner;

            public ViewHolder(View itemView) {
                super(itemView);
                comment = itemView.findViewById(R.id.comment);
                commentLayout = itemView.findViewById(R.id.commentLayout);
                commentOwner = itemView.findViewById(R.id.commentOwner);
            }
        }
}
