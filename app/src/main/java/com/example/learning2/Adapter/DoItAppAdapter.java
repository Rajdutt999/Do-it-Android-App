package com.example.learning2.Adapter;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.learning2.AddNewTask;
import com.example.learning2.Model.DoItAppModel;
import com.example.learning2.MainActivity;
import com.example.learning2.R;
import com.example.learning2.Utils.DatabaseHandler;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;



public class DoItAppAdapter extends RecyclerView.Adapter<DoItAppAdapter.ViewHolder>{

    private List<DoItAppModel> todoList;

    private final MainActivity activity;
    private final DatabaseHandler db;

    public DoItAppAdapter(DatabaseHandler db , MainActivity activity) {

        this.db = db;

        this.activity=activity;
    }

    /*
        set layout view
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout,parent,false);


        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        db.openDatabase();

        // get the item position
        //recyclerview entry
        final DoItAppModel item = todoList.get(position);

        holder.task.setText(item.getTask());

        holder.task1.setText(item.getTaskDetails());

        holder.subj.setText(item.getSubject());

        holder.dueDate.setText(item.getDueDate());
        holder.dueTime.setText(item.getDueTime());


        // Date format :
        // HH for 24 hr clock format
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        Date d1=null;
        Date d2 =new Date();
        Date d3=null;

        //holder.dueDate.setText(formatter.format(d2).substring(0,10));

        try {

            // get the date value
            // parse to
            // convert string to Date

            d1 =new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(
                    holder.dueDate.getText().toString() +
                            " " +holder.dueTime.getText().toString());



            //set textview
            //get the format required
            //
            holder.dueTime.setText(formatter.format(d1).substring(11) );

            //get current date time
            //d2
            //format and parse d2 to get date time
            //comparable to d1
            d3= new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(formatter.format(d2));

            if(d1.compareTo(d3)<0)
            {
               // holder.dueDate.setText(formatter.format(d3));

                holder.dueDate.setTextColor(ContextCompat.getColor(getContext(), R.color.missing_red));
                holder.dueTime.setTextColor(ContextCompat.getColor(getContext(), R.color.missing_red));

            }
        } catch (ParseException e) {

            e.printStackTrace();

        }
        //holder.subj.setText(item.getSubject());

        holder.task1.setChecked(toBoolean(item.getStatus()));

        holder.task1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);

                }
                else{
                    db.updateStatus(item.getId(),0);

                }
            }

        });
    }

    @Override
    public int getItemCount(){

        return todoList.size();
    }

    private boolean toBoolean(int n){

        return n!=0;
    }


    public void setTasks(List<DoItAppModel> todoList){

        this.todoList = todoList;
        notifyDataSetChanged();
    }


    public Context getContext(){

        return activity;
    }


    public void deleteItem(int position){

        //get the position of the item to be deleted
        DoItAppModel item = todoList.get(position);

        db.deleteTask(item.getId());
        //remove from list
        todoList.remove(position);

        notifyItemRemoved(position);

    }

    public void editItem(int position){

        // get the position of the item to be edited
        DoItAppModel item = todoList.get(position);

        Bundle bundle = new Bundle();

        //put current values in the bundle
        //        ("key",value)
        bundle.putInt("id",item.getId());

        bundle.putString("task",item.getTask());
        bundle.putString("taskDetails",item.getTaskDetails());

        bundle.putString("subject",item.getSubject());

        bundle.putString("time",item.getDueTime());
        bundle.putString("date",item.getDueDate());

        AddNewTask fragment = new AddNewTask();

        //set the current task details to be edited
        //open AddNewTask...
        //Bottom Dialogue fragment
        fragment.setArguments(bundle);

        fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);


    }
    public static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView task;
        TextView dueDate;
        TextView dueTime;
        TextView subj;

        CheckBox task1;

        ViewHolder(View view){
            super(view);

            //register all item views
            task = view.findViewById(R.id.textName);

            task1 = view.findViewById(R.id.todoCheckBox);

            subj = view.findViewById(R.id.subject);

            dueDate = view.findViewById(R.id.DueDate);
            dueTime = view.findViewById(R.id.DueTime);
        }
    }

}
