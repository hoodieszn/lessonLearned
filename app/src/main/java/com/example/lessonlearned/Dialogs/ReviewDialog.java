package com.example.lessonlearned.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.lessonlearned.R;

public class ReviewDialog extends DialogFragment {
    public interface ReviewDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String reason, double rating);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ReviewDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_review, null);

        builder.setView(view).setMessage("Write a Tutor Review");

        final RatingBar ratingBar = view.findViewById(R.id.ratingReview);
        ratingBar.setStepSize(0.5f);

        final EditText reviewText = view.findViewById(R.id.reviewReason);

        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                String reviewString = "";

                try {
                    reviewString = reviewText.getText().toString();
                } catch (NullPointerException e) {
                }

                double rating = ratingBar.getRating();

                // Send the positive button event back to the host activity
                listener.onDialogPositiveClick(ReviewDialog.this, reviewString, rating);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick(ReviewDialog.this);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ReviewDialogListener so we can send events to the host
            listener = (ReviewDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            Log.d("DIALOGERROR", e.toString() + " must implement ReviewDialogListener");
        }
    }
}
