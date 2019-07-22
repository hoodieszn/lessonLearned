package com.example.lessonlearned.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lessonlearned.Models.Course;
import com.example.lessonlearned.Models.Degree;
import com.example.lessonlearned.Models.Tutor;
import com.example.lessonlearned.R;
import com.example.lessonlearned.Services.RESTClientRequest;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePostingDialog extends DialogFragment {
    public interface CreatePostingDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    CreatePostingDialog.CreatePostingDialogListener listener;

    private EditText aboutText, priceText;
    private Spinner degreesSpinner;
    private TextView courseText, loadingText;
    private AutoCompleteTextView autocomplete;
    private FlexboxLayout courseContainer;
    private ProgressBar spinner;

    private List<Degree> degrees = new ArrayList<>();
    private Map<String, Integer> degreeMap = new HashMap<String, Integer>();
    private int degreeSelected;

    private List<Course> courses = new ArrayList<>();
    private Map<String, Integer> courseMap = new HashMap<String, Integer>();

    private List<Course> selectedCourses = new ArrayList<>();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_createposting, null);

        aboutText = view.findViewById(R.id.aboutText);
        priceText = view.findViewById(R.id.price);
        degreesSpinner = view.findViewById(R.id.degreesSpinner);
        courseText = view.findViewById(R.id.degreeCourses);
        autocomplete = view.findViewById(R.id.autocomplete_courses);
        courseContainer = view.findViewById(R.id.courseContainer);
        spinner = view.findViewById(R.id.addpostingSpinner);
        loadingText = view.findViewById(R.id.loadingText);

        // Fetch Degrees from server
        try {
            startLoadingState();
            RESTClientRequest.getDegreesList(this, com.example.lessonlearned.Singletons.Context.getUser().getSchoolId());
        }
        catch (JSONException e){
            Log.d("JSONException", e.toString());
        }

        builder.setView(view).setTitle("Create a New Posting");

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                try {
                    String postText = aboutText.getText().toString();
                    String price = priceText.getText().toString();

                    RESTClientRequest.postPosting(degreeSelected, (Tutor) com.example.lessonlearned.Singletons.Context.getUser(),
                            Double.parseDouble(price), postText, selectedCourses, CreatePostingDialog.this);
                }
                catch (JSONException e){
                    Log.d("JSONException", e.toString());
                }
                catch (NullPointerException e){ }

            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                try {
                    listener.onDialogNegativeClick(CreatePostingDialog.this);
                }
                catch (NullPointerException e){ }

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
            listener = (CreatePostingDialog.CreatePostingDialogListener) context;
        }
        catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            Log.d("DIALOGERROR", e.toString() + " must implement CreatePostingDialogListener");
        }
    }

    public void refreshPostings(){
        listener.onDialogPositiveClick(this);
    }

    public void loadDegreeList(List<Degree> degrees){
        this.degrees = degrees;
        this.degreeMap = new HashMap<String, Integer>();

        for (int i=0; i < degrees.size(); i++) degreeMap.put(degrees.get(i).getName(), degrees.get(i).getId());
        ArrayList<String> degreeNames = new ArrayList<String>(degreeMap.keySet());

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.sort_spinner, degreeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        degreesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String degreeName = parent.getItemAtPosition(position).toString();
                degreeSelected = degreeMap.get(degreeName);
                setDegreeSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }

        });

        degreesSpinner.setAdapter(adapter);
    }

    public void setDegreeSelected(){
        //get courses for the degree
        try {
            startLoadingState();
            RESTClientRequest.getCoursesByDegree(this, degreeSelected);
        }
        catch (JSONException e){
            Log.d("JSONException", e.toString());
        }
    }

    public void setCourseList(List<Course> coursesForThisDegree) {
        this.courses = coursesForThisDegree;
    }

    public void initCourseAutofill() {
        courseMap = new HashMap<String, Integer>();
        for (int i=0; i < courses.size(); i++) courseMap.put(courses.get(i).getName(), courses.get(i).getId());
        ArrayList<String> courseNames = new ArrayList<String>(courseMap.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, courseNames);
        autocomplete.setAdapter(adapter);

        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String courseName = parent.getItemAtPosition(position).toString();
                int courseId = courseMap.get(courseName);

                autocomplete.getText().clear();

                Course selectedCourse = new Course(courseId, courseName, com.example.lessonlearned.Singletons.Context.getUser().getSchoolId());
                addSelectedCourse(selectedCourse);
            }
        });
    }

    private void addSelectedCourse(final Course course) {

        //Add course to view
        selectedCourses.add(course);

        ViewGroup.LayoutParams params = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ((RelativeLayout.LayoutParams) params).setMargins(5, 5, 5, 5);

        final Button courseName = new Button(this.getActivity());

        courseName.setLayoutParams(params);
        courseName.setText(course.getName() + "  X");
        courseName.setBackgroundResource(R.drawable.course_code_bg);
        courseName.setTextSize(14);
        courseName.setTextColor(Color.WHITE);

        courseContainer.addView(courseName);
        courseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove course from selected courses list
                selectedCourses.remove(course);
                courseName.setVisibility(View.GONE);
            }
        });
    }

    public void clearSelectedCourses(){
        selectedCourses = new ArrayList<>();
        courseContainer.removeAllViews();
    }

    public void startLoadingState(){
        aboutText.setEnabled(false);
        priceText.setEnabled(false);
        degreesSpinner.setEnabled(false);
        autocomplete.setEnabled(false);

        spinner.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.VISIBLE);
    }

    public void stopLoadingState(){
        aboutText.setEnabled(true);
        priceText.setEnabled(true);
        degreesSpinner.setEnabled(true);
        autocomplete.setEnabled(true);

        spinner.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
    }
}