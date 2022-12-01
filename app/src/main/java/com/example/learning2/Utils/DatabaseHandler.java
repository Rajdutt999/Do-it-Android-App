package com.example.learning2.Utils;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.learning2.Model.DoItAppModel;

import java.util.ArrayList;
import java.util.List;


/**
 * main task database
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    private static final String NAME =  "toDoListDatabase";
    private static final String TODO_TABLE = "todo";

    private static final String ID = "id";
    private static final String TASK = "taskTEXT";
    private static final String TASKDETAILS = "taskDetails";
    private static final String SUBJECT = "subject";
    private static final String STATUS = "status";
    private static final String DATE = "date";
    private static final String TIME = "time";

    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT," +
            TASKDETAILS + " TEXT," + SUBJECT + " TEXT," + DATE + " TEXT," + TIME + " TEXT," + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context){
        super(context, NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion , int newVersion){
        //Drop the older table if existed
        db.execSQL("Drop TABLE IF EXISTS " + TODO_TABLE);
        //Create tables again
        onCreate(db);
    }

    public  void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(DoItAppModel task){

        ContentValues cv = new ContentValues();

        cv.put(TASK, task.getTask());
        cv.put(TASKDETAILS, task.getTaskDetails());

        cv.put(SUBJECT, task.getSubject());

        cv.put(DATE, task.getDueDate());
        cv.put(TIME, task.getDueTime());

        cv.put(STATUS,0);

        db.insert(TODO_TABLE, null,cv);

    }

    public List<DoItAppModel> getallTasks(){

        List<DoItAppModel> tasklist = new ArrayList<>();
        Cursor cur = null;

        db.beginTransaction();

        try{

            cur = db.query(TODO_TABLE,null,null,null,null,null,null,null);

            if(cur != null){

                if(cur.moveToFirst()){

                    do{
                        DoItAppModel task = new DoItAppModel();

                        task.setId(cur.getInt(cur.getColumnIndex(ID)));

                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setTaskDetails(cur.getString(cur.getColumnIndex(TASKDETAILS)));

                        task.setSubject(cur.getString(cur.getColumnIndex(SUBJECT)));

                        task.setDueDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setDueTime(cur.getString(cur.getColumnIndex(TIME)));

                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));

                        tasklist.add(task);

                    }while(cur.moveToNext());

                }
            }

        }
        finally {

            db.endTransaction();

            assert cur != null;
            cur.close();
        }

        return tasklist;
    }

    public List<DoItAppModel> getallTasksfiltered(){

        List<DoItAppModel> tasklist = new ArrayList<>();
        Cursor cur = null;

        db.beginTransaction();

        try{

            cur = db.query(TODO_TABLE,null,null,null,null,null,null,null);

            if(cur != null){

                if(cur.moveToFirst()){

                    do{
                        DoItAppModel task = new DoItAppModel();

                        task.setSubject(cur.getString(cur.getColumnIndex(SUBJECT)));

                        if(task.getSubject().trim().equals("Subjects")) {
                            task.setId(cur.getInt(cur.getColumnIndex(ID)));

                            task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                            task.setTaskDetails(cur.getString(cur.getColumnIndex(TASKDETAILS)));

                            task.setDueDate(cur.getString(cur.getColumnIndex(DATE)));
                            task.setDueTime(cur.getString(cur.getColumnIndex(TIME)));

                            task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));

                            tasklist.add(task);

                        }

                    }while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return tasklist;
    }


    public List<DoItAppModel> getallTasksSubjSort(){

        List<DoItAppModel> tasklist = new ArrayList<>();
        Cursor cur = null;

        db.beginTransaction();

        try{
            cur = db.query(TODO_TABLE, null,null,null,null,null,  SUBJECT+" DESC" ,null);

            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        DoItAppModel task = new DoItAppModel();

                        task.setSubject(cur.getString(cur.getColumnIndex(SUBJECT)));

                        task.setId(cur.getInt(cur.getColumnIndex(ID)));

                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setTaskDetails(cur.getString(cur.getColumnIndex(TASKDETAILS)));

                        task.setDueDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setDueTime(cur.getString(cur.getColumnIndex(TIME)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));

                        tasklist.add(task);


                    }while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;

            cur.close();
        }

        return tasklist;
    }


    public List<DoItAppModel> getallTasksSorted(){

        List<DoItAppModel> tasklist = new ArrayList<>();
        Cursor cur = null;

        db.beginTransaction();

        try{
            cur = db.query(TODO_TABLE, null,null,null,null,null,  DATE+" DESC, " +TIME+" DESC" ,null);

            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        DoItAppModel task = new DoItAppModel();

                        task.setSubject(cur.getString(cur.getColumnIndex(SUBJECT)));

                            task.setId(cur.getInt(cur.getColumnIndex(ID)));

                            task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                            task.setTaskDetails(cur.getString(cur.getColumnIndex(TASKDETAILS)));

                            task.setDueDate(cur.getString(cur.getColumnIndex(DATE)));
                            task.setDueTime(cur.getString(cur.getColumnIndex(TIME)));

                            task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));

                            tasklist.add(task);


                    }while(cur.moveToNext());
                }
            }
        }
        finally {

            db.endTransaction();
            assert cur != null;

            cur.close();
        }

        return tasklist;
    }


    public List<DoItAppModel> getallPendingTasks(){

        List<DoItAppModel> tasklist = new ArrayList<>();
        Cursor cur = null;

        db.beginTransaction();

        try{
            cur = db.query(TODO_TABLE,null,null,null,null,null,null,null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        DoItAppModel task = new DoItAppModel();

                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));

                        if(task.getStatus()== 0) {

                            task.setId(cur.getInt(cur.getColumnIndex(ID)));

                            task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                            task.setTaskDetails(cur.getString(cur.getColumnIndex(TASKDETAILS)));

                            task.setSubject(cur.getString(cur.getColumnIndex(SUBJECT)));

                            task.setDueDate(cur.getString(cur.getColumnIndex(DATE)));
                            task.setDueTime(cur.getString(cur.getColumnIndex(TIME)));


                            tasklist.add(task);
                        }


                    }while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;

            cur.close();
        }

        return tasklist;
    }



    public List<DoItAppModel> getPendingTasksSorted(){

        List<DoItAppModel> tasklist = new ArrayList<>();
        Cursor cur = null;

        db.beginTransaction();

        try{
            cur = db.query(TODO_TABLE, null,null,null,null,null,  DATE+" DESC, " +TIME+" DESC" ,null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        DoItAppModel task = new DoItAppModel();

                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));

                        if(task.getStatus()== 0) {
                            task.setId(cur.getInt(cur.getColumnIndex(ID)));

                            task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                            task.setTaskDetails(cur.getString(cur.getColumnIndex(TASKDETAILS)));

                            task.setSubject(cur.getString(cur.getColumnIndex(SUBJECT)));

                            task.setDueDate(cur.getString(cur.getColumnIndex(DATE)));
                            task.setDueTime(cur.getString(cur.getColumnIndex(TIME)));


                            tasklist.add(task);
                        }


                    }while(cur.moveToNext());
                }
            }
        }
        finally {

            db.endTransaction();
            assert cur != null;

            cur.close();
        }

        return tasklist;
    }



    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS,status);

        db.update(TODO_TABLE, cv , ID +"= ?", new String[] {String.valueOf(id)});

    }


    public void updateTask(int id, String task,String task_details, String subject, String date, String time){

        ContentValues cv = new ContentValues();

        cv.put(TASK,task);
        cv.put(TASKDETAILS, task_details);

        cv.put(SUBJECT,subject);

        cv.put(DATE ,date);
        cv.put(TIME,time);

        db.update(TODO_TABLE, cv ,ID +"= ?", new String[] {String.valueOf(id)});
    }



    public void deleteTask(int id){

        db.delete(TODO_TABLE , ID + "= ?", new String[] {String.valueOf(id)});

        //String sql = "UPDATE "+TABLE_NAME +" SET " + ColumnName+ " = '"+newValue+"' WHERE "+Column+ " = "+rowId;
    }


}
