package com.lessonlearned.app.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.lessonlearned.app.R;

public class ReportAbuseDialog extends DialogFragment {
    public interface ReportAbuseDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String reason);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    ReportAbuseDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_report_abuse, null);

        builder.setView(view).setMessage("Report Abuse");

        final EditText reportText = view.findViewById(R.id.abuseReason);

        builder.setPositiveButton("Report", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                String reportString = "";

                try {
                    reportString= reportText.getText().toString();
                }
                catch (NullPointerException e){ }
                // Send the positive button event back to the host activity
                listener.onDialogPositiveClick(ReportAbuseDialog.this, reportString);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                // Send the negative button event back to the host activity
                listener.onDialogNegativeClick(ReportAbuseDialog.this);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the CreatePostingDialogListener so we can send events to the host
            listener = (ReportAbuseDialogListener) context;
        }
        catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            // Log.d("DIALOGERROR", e.toString() + " must implement CreatePostingDialogListener");
        }
    }
}