package com.example.learning2;


import android.app.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.content.DialogInterface;

import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;

import com.example.learning2.Model.DoItAppModel;
import com.example.learning2.Utils.DatabaseHandler;
import com.example.learning2.Utils.SubjDatabaseHandler;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Adding new task Activity
 */
public class AddNewTask extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {
    public static final  String TAG = "ActionBottomDialog";
                    //edit task

    Spinner spinner;

    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private EditText newTaskText, newTaskDescription, in_date, in_time;
    private Button newTaskSaveButton;
    private DatabaseHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set style -> dialogStyle
        setStyle(STYLE_NORMAL, R.style.DialogStyle);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.new_task, container , false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        spinner = view.findViewById(R.id.spinner1);

        spinner.setOnItemSelectedListener(this);

        // Reload Spinner
        loadSpinnerData();

        //get date and time in text format
        txtDate = (EditText) view.findViewById(R.id.in_date);
        txtTime = (EditText) view.findViewById(R.id.in_time);


        txtDate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick (View v){

                if (v == txtDate) {

                    // Get Current Date
                    //set the default value for the picker
                    final Calendar c = Calendar.getInstance();

                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    if((monthOfYear+1 )<10 && dayOfMonth<10)
                                        txtDate.setText("0"+dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                                    else if((monthOfYear+1 )<10)
                                        txtDate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);

                                    else if(dayOfMonth<10)
                                        txtDate.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    else
                                        txtDate.setText(dayOfMonth + "-" + (monthOfYear +1) + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    //date picker : calendar view
                    datePickerDialog.show();
                }

            }

        });

        txtTime.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick (View v){

                if (v == txtTime) {

                    // Get Current Time
                    //set default time as current time
                    final Calendar c = Calendar.getInstance();

                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    if(hourOfDay<10 && minute <10) {
                                        txtTime.setText("0" + hourOfDay + ":0" + minute);
                                    }
                                    else if(hourOfDay < 10){
                                        txtTime.setText("0" + hourOfDay + ":" + minute);
                                    }
                                    else if(minute < 10){
                                        txtTime.setText(hourOfDay + ":0" + minute);
                                    }
                                    else{
                                        txtTime.setText(hourOfDay + ":" + minute);
                                    }
                                }
                            }, mHour, mMinute, false);
                                                        //set 12hr clock with am/pm
                    //tme picker : clock view
                    timePickerDialog.show();
                }
            }

        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // register item views
        newTaskText = getView().findViewById(R.id.newTaskText);
        newTaskDescription = getView().findViewById(R.id.newTaskDescription);

        in_date = getView().findViewById(R.id.in_date);
        in_time = getView().findViewById(R.id.in_time);

        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;

        final Bundle bundle = getArguments();

        //case for edit task
        if (bundle != null) {

            isUpdate = true;

            //extract the bundle values
            //values to be updated
            String task = bundle.getString("task");
            String taskDetails = bundle.getString("taskDetails");

            String subject = bundle.getString("subject");

            String date = bundle.getString("date");
            String time = bundle.getString("time");

            //set all values for
            newTaskText.setText(task);
            newTaskDescription.setText(taskDetails);

            //cast spinner to an ArrayAdapter
            ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter();

            //get extracted subject value position
            int spinnerPosition = myAdap.getPosition(subject);

            //set the subject with the position
            spinner.setSelection(spinnerPosition);

            txtDate.setText(date);
            txtTime.setText(time);

            if (task.length() > 0)
                //show that save button is enabled
                ///with color change
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));//colors.xml
        }

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //pass
                //stay disabled
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().equals("")){

                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);

                }
                else{
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


        final boolean finalIsUpdate = isUpdate;

        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text for db insert
                String text = newTaskText.getText().toString();
                String taskDetails = newTaskDescription.getText().toString();
                String subject = spinner.getSelectedItem().toString();
                String date = in_date.getText().toString();
                String time = in_time.getText().toString();
                //String text = newTaskText.getText().toString();

                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"),text,taskDetails,subject,date,time);
                }
                else {
                    //case: adding new task
                    DoItAppModel task = new DoItAppModel();

                    task.setTask(text);
                    task.setTaskDetails(taskDetails);

                    task.setSubject(subject);

                    task.setDueDate(date);
                    task.setDueTime(time);

                    task.setStatus(0);

                    db.insertTask(task);
                }

                Date d1=null;
                Date d2 = new Date();
                Date d3=null;

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                try {

                    d1 =new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(in_date.getText().toString() +
                            " " +in_time.getText().toString());


                    //holder.dueTime.setText(formatter.format(d1).substring(11) );

                    d3= new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(formatter.format(d2));


                } catch (ParseException e) {
                    e.printStackTrace();

                }
                long diff =d1.getTime() -d3.getTime();

                //on save
                //dismiss the dilogue fragment
                dismiss();
            }
        });

    }

    /**
     * close bottomsheet dilogFragment
     */
    @Override
    public void onDismiss(DialogInterface dialog){

        Activity activity = getActivity();

        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }



    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {

        //open the subject databaseHandler
        SubjDatabaseHandler db = new SubjDatabaseHandler(getActivity().getApplicationContext());
        List<String> labels = db.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - subject list
        //item clickable
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching subject adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    /**
     * spinner item selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Select spinner item
        //get item
        //label for database entry
        String label = parent.getItemAtPosition(position).toString();

        String subject1 = label;

        //Toast.makeText(parent.getContext(), "You selected: " + label, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        // no action
        //implemented AdapterView.onItemSelectedListener

    }


}
