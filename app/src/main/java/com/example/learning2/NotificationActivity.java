package com.example.learning2;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;

import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import android.view.View;


import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Set Notification Reminders
 */
public class NotificationActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "10002" ;
    private final static String default_notification_channel_id = "default" ;

    EditText task_reminder ;
    TextView Due_Date ;
    TextView Due_Time ;

    Button set_notification;

    private int mYear, mMonth, mDay, mHour, mMinute;

    final Calendar myCalendar = Calendar. getInstance () ;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super .onCreate(savedInstanceState) ;
        setContentView(R.layout. notification_reminder ) ;

        Objects.requireNonNull(getSupportActionBar()).hide();


        task_reminder = findViewById(R.id. task_reminder ) ;
        Due_Date = findViewById(R.id.in_DueDate);
        Due_Time = findViewById(R.id.in_DueTime);
        set_notification = findViewById(R.id.set_notification);


        /**
         *
         get due date and time for notifications
         *
         **/

        Due_Date.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick (View v){

                if (v == Due_Date) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(NotificationActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    if((monthOfYear+1 )<10 && dayOfMonth<10)
                                        Due_Date.setText("0"+dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                    else if((monthOfYear+1 )<10)
                                        Due_Date.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                    else if(dayOfMonth<10)
                                        Due_Date.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                    else
                                        Due_Date.setText(dayOfMonth + "-" + (monthOfYear +1) + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }

            }

        });


        Due_Time.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick (View v){

                if (v == Due_Time) {

                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(NotificationActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    if(hourOfDay<10 && minute <10) {
                                        Due_Time.setText("0" + hourOfDay + ":0" + minute);
                                    }
                                    else if(hourOfDay < 10){
                                        Due_Time.setText("0" + hourOfDay + ":" + minute);
                                    }
                                    else if(minute < 10){
                                        Due_Time.setText(hourOfDay + ":0" + minute);
                                    }
                                    else{
                                        Due_Time.setText(hourOfDay + ":" + minute);
                                    }
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            }

        });


        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        set_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Due_Time.getText()!=null && Due_Date.getText() !=null) {

                    Date d1 = null;

                    try {

                        d1 = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(Due_Date.getText().toString() +
                                " " + Due_Time.getText().toString());

                        //Due_Time.setText(formatter.format(d1).substring(11) );

                        updateLabel(d1);

                    } catch (ParseException e) {
                        e.printStackTrace();

                    }
                }
                else{
                    //pass;
                    Toast.makeText(NotificationActivity.this, "Err: Date or Time field empty" ,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    public void scheduleNotification (Notification notification , long delay) {

        Intent notificationIntent = new Intent( this, DoItNotificationPublisher.class ) ;

        notificationIntent.putExtra(DoItNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(DoItNotificationPublisher. NOTIFICATION , notification) ;

        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;

        assert alarmManager != null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {


            long c =(System.currentTimeMillis()/60000);
            c =(long) Math.floor(c);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c*60000 + delay, pendingIntent);


            Toast.makeText(this, "Reminder Added Successfully" , Toast.LENGTH_SHORT).show();

        } else {

            long c =(System.currentTimeMillis()/60000);
            c =(long) Math.floor(c);

            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, c*60000 +delay, pendingIntent);

            Toast.makeText(this, "Reminder Added" , Toast.LENGTH_SHORT).show();

        }

        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP , System.currentTimeMillis()/10000, pendingIntent) ;
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP , delay , pendingIntent) ;

    }
    public Notification getNotification (String content) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;

        builder.setContentTitle( "DoIt : Task Due" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_mine_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;

        return builder.build() ;

    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet (DatePicker view , int year , int monthOfYear , int dayOfMonth) {
            myCalendar .set(Calendar. YEAR , year) ;
            myCalendar .set(Calendar. MONTH , monthOfYear) ;
            myCalendar .set(Calendar. DAY_OF_MONTH , dayOfMonth) ;

        }
    } ;


    public void setDate (View view) {
        new DatePickerDialog(
                NotificationActivity. this, date ,
                myCalendar .get(Calendar. YEAR ) ,
                myCalendar .get(Calendar. MONTH ) ,
                myCalendar .get(Calendar. DAY_OF_MONTH )
        ).show() ;
    }


    public void updateLabel(Date d) {

        String DateFormat = "dd-MM-yy HH:mm" ;
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat , Locale. getDefault ()) ;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date Duedate = myCalendar.getTime() ;
        Date current_Date = new Date() ;

        //current_Date = formatter.format(current_Date);
        try {

            current_Date = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(formatter.format(current_Date));

        } catch (ParseException e) {
            e.printStackTrace();

        }

        long diff = d.getTime()-current_Date.getTime();

        scheduleNotification(getNotification( task_reminder.getText().toString()) , diff) ;

        task_reminder.setText("");
        Due_Date.setText("");
        Due_Time.setText("");
    }
}

