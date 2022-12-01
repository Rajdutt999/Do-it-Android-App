package com.example.learning2;


import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;


import android.widget.PopupMenu;
import android.widget.Button;
import android.widget.Toast;


import com.example.learning2.Adapter.DoItAppAdapter;
import com.example.learning2.Model.DoItAppModel;
import com.example.learning2.Utils.DatabaseHandler;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener, PopupMenu.OnMenuItemClickListener{

    private DatabaseHandler db;

    private RecyclerView taskRecyclerView;
    private DoItAppAdapter tasksAdapter;
    private FloatingActionButton fab;

    private List<DoItAppModel> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();


        db = new DatabaseHandler(this);
        db.openDatabase();


        /*
            Register the recyclerView
         */
        taskRecyclerView = findViewById(R.id.taskRecyclerView);

        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
            set adapter
         */
        tasksAdapter = new DoItAppAdapter(db,MainActivity.this);
        taskRecyclerView.setAdapter(tasksAdapter);


        /*
            set itemtouch helper for the recycle view
            to

            RecyclerItemTouchHelper
         */

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        fab = findViewById(R.id.fab);


        taskList = db.getallTasks();
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });


        /**
         *  menu
         *
         *  */
        Button btn = (Button) findViewById(R.id.btnShow);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(MainActivity.this, v);

                popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) MainActivity.this);
                popup.inflate(R.menu.popup_menu);

                popup.show();
            }

        });

        /*
            sort and filter
         */
        Button menuBtn = (Button) findViewById(R.id.menuBtn);


        menuBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(MainActivity.this, v);

                popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) MainActivity.this);
                popup.inflate(R.menu.popup_menu1);

                popup.show();
            }
        });





    }
    private static final int i=1;

    @Override
    public boolean onMenuItemClick(MenuItem item) {


        //Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.Subjects:
                /*
                    subject activity
                 */
                Intent i = new Intent( MainActivity.this, Subjects.class);
                startActivity(i);
                return true;

            case R.id.schedule:
                /*
                    schedule activity
                 */
                Intent j = new Intent( MainActivity.this, Schedules.class);
                startActivity(j);
                return true;

            case R.id.reminder:
                /*
                    switch Activity to reminder
                 */
                Intent k = new Intent( MainActivity.this, NotificationActivity.class);
                startActivity(k);
                return true;

            case R.id.id_none:
                /*
                    remove all filters and sorts ..
                 */
                taskRecyclerView.setAdapter(tasksAdapter);
                taskList = db.getallTasks();
                Collections.reverse(taskList);

                tasksAdapter.setTasks(taskList);
                return true;

            case R.id.filter_pending:
                /*
                    View only pending tasks
                 */
                taskRecyclerView.setAdapter(tasksAdapter);

                taskList = db.getallPendingTasks();
                Collections.reverse(taskList);

                tasksAdapter.setTasks(taskList);
                return true;

            case R.id.pending_due_sort:
                /*
                    View only pending tasks with sorted due date
                 */
                taskRecyclerView.setAdapter(tasksAdapter);

                taskList = db.getPendingTasksSorted();
                Collections.reverse(taskList);

                tasksAdapter.setTasks(taskList);
                return true;

            case R.id.sort_subj:
                /*
                    sort :  subjectwise
                 */
                taskRecyclerView.setAdapter(tasksAdapter);

                taskList = db.getallTasksSubjSort();
                Collections.reverse(taskList);

                tasksAdapter.setTasks(taskList);
                return true;

            case R.id.sort_due:
                /*
                sort : earliest due task
                 */
                taskRecyclerView.setAdapter(tasksAdapter);

                taskList = db.getallTasksSorted();
                Collections.reverse(taskList);

                tasksAdapter.setTasks(taskList);
                return true;


            case R.id.refresh:

                tasksAdapter = new DoItAppAdapter(db,MainActivity.this);

                taskRecyclerView.setAdapter(tasksAdapter);
                taskList = db.getallTasks();

                Collections.reverse(taskList);

                tasksAdapter.setTasks(taskList);

                Toast.makeText(this, "Activity Refreshed", Toast.LENGTH_SHORT).show();

                return true;

            default:

                return false;

        }
    }




    @Override
    public void handleDialogClose(DialogInterface dialog){

        taskList = db.getallTasks();

        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();

    }



/*
    private static final int MENU3 = 1;
    private static final int MENU4 = 2;
    private static final int SUBMENU1 = 3;
    private static final int SUBMENU2 = 4;
    private static final int SUBMENU3 = 5;
    private static final int GROUP1 = 6;
    private static final int MENU5 = 7;
    private static final int MENU6 = 8;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.w("ANDROID MENU TUTORIAL:", "onCreateOptionsMenu(Menu menu)");

        MenuItem menu3 = menu.add(Menu.NONE, MENU3, 3, "Menu No. 3");
        menu3.setAlphabeticShortcut('c');

        SubMenu menu4 = menu.addSubMenu(Menu.NONE, MENU4, 4,"Menu No. 4");
        menu4.add(GROUP1, SUBMENU1, 1, "SubMenu No. 1");
        menu4.add(GROUP1, SUBMENU2, 2, "SubMenu No. 2");
        menu4.setGroupCheckable(GROUP1,true,true);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;

    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        Log.w("ANDROID MENU TUTORIAL:", "onPrepareOptionsMenu(Menu menu)");
        MenuItem menu5 = menu.findItem(MENU5);
        if(menu5 == null){
            menu5 = menu.add(Menu.NONE, MENU5, 5, "Menu No. 5");
        }
        MenuItem menu6 = menu.findItem(MENU6);
        if(menu6 == null){
            menu6 = menu.add(Menu.NONE, MENU6, 5, "Menu No. 6");
        }

        MenuItem menu2 = menu.findItem(R.id.menu2);
        SubMenu subMenu2 = menu2.getSubMenu();

        MenuItem subMenuItem3 = menu.findItem(SUBMENU3);
        if(subMenuItem3 == null){
            subMenu2.add(R.id.group2, SUBMENU3, 3, "SubMenu No. 3");
            subMenu2.setGroupCheckable(R.id.group2,true,true);
        }

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.w("ANDROID MENU TUTORIAL:", "onOptionsItemSelected(MenuItem item)");

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu1:
                Toast.makeText(this, "Clicked: Menu No. 1", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu2:
                Toast.makeText(this, "Clicked: Menu No. 2", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.submenu1:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                Toast.makeText(this, "Clicked: Menu No. 2 - SubMenu No .1", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.submenu2:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                Toast.makeText(this, "Clicked: Menu No. 2 - SubMenu No .2", Toast.LENGTH_SHORT).show();
                return true;
            case MENU3:
                Toast.makeText(this, "Clicked: Menu No. 3", Toast.LENGTH_SHORT).show();
                return true;
            case SUBMENU1:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                Toast.makeText(this, "Clicked: Menu No. 4 - SubMenu No .1", Toast.LENGTH_SHORT).show();
                return true;
            case SUBMENU2:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                Toast.makeText(this, "Clicked: Menu No. 4 - SubMenu No .2", Toast.LENGTH_SHORT).show();
                return true;
            case SUBMENU3:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                Toast.makeText(this, "Clicked: Menu No. 2 - SubMenu No .3", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }*/

}