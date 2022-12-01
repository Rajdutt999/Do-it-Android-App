package com.example.learning2;


import android.content.Context;

import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import android.view.View;
import android.view.inputmethod.InputMethodManager;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learning2.Utils.DatabaseHandler;
import com.example.learning2.Utils.SubjDatabaseHandler;

import java.util.List;
import java.util.Objects;

/**
 *  Manage Subjects
 */
public class Subjects extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    Button btnAdd;
    Button btnDelete;
    EditText inputLabel;

    String subject1 ="";

    private SubjDatabaseHandler db;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjects);
        Objects.requireNonNull(getSupportActionBar()).hide();


        db = new SubjDatabaseHandler(this);
        db.openDatabase();

        spinner = findViewById(R.id.spinner);

        btnAdd =  findViewById(R.id.btn_add);
        btnDelete =  findViewById(R.id.btn_delete);

        inputLabel = findViewById(R.id.input_label);

        spinner.setOnItemSelectedListener(this);

        // Loading spinner data from database
        loadSpinnerData();

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String label = inputLabel.getText().toString();

                if (label.trim().length() > 0) {
                    SubjDatabaseHandler db = new SubjDatabaseHandler(getApplicationContext());
                    db.insertLabel(label);

                    // making input filed text to blank
                    inputLabel.setText("");

                    // Hiding the keyboard
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);

                    // loading spinner with newly added data
                    loadSpinnerData();
                } else {

                    Toast.makeText(getApplicationContext(), "Please enter subject ",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter

                int spinnerPosition = myAdap.getPosition(subject1);

                //SubjDatabaseHandler db = new SubjDatabaseHandler(getApplicationContext());
                spinner.setSelection(0);


               // ((List<String>) spinner.getAdapter()).notifyDataSetChanged();
                db.deleteSubject(spinnerPosition+1);

                //((List<String>)spinner.getAdapter()).remove((String)spinner.getSelectedItem());
                loadSpinnerData();

                Toast.makeText(Subjects.this, "Subject deleted" ,
                          Toast.LENGTH_LONG).show();

            }
        });
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        SubjDatabaseHandler db = new SubjDatabaseHandler(getApplicationContext());
        List<String> labels = db.getAllLabels();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        subject1 = label;


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
