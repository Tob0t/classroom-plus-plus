package ex5.it.mcm.fhooe.classroomplusplus.ui.availability;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import ex5.it.mcm.fhooe.classroomplusplus.R;
import ex5.it.mcm.fhooe.classroomplusplus.model.Lecture;
import ex5.it.mcm.fhooe.classroomplusplus.ui.MainActivity;
import ex5.it.mcm.fhooe.classroomplusplus.utils.Constants;
import ex5.it.mcm.fhooe.classroomplusplus.utils.DataService;
import ex5.it.mcm.fhooe.classroomplusplus.utils.Helper;

public class ReserveRoomActivity extends AppCompatActivity {
    private EditText mStartTime, mName, mEndTime;
    private Button mMakeReservation;
    private TextView mRoomIdTextView;
    private TimePickerDialog mStartTimePickerDialog, mEndTimePickerDialog;
    private String mRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_room);


         /* Get the push ID from the extra passed by previous Activity */
        Intent intent = this.getIntent();
        mRoomId = intent.getStringExtra(Constants.KEY_ROOM_ID);

        initializeScreen();
        setDateTimeFields();


    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.editTextStartTime: {
                    mStartTimePickerDialog.show();
                    break;
                }
                case R.id.editTextEndTime: {
                    mEndTimePickerDialog.show();
                    break;
                }
                case R.id.buttonMakeReservation: {
                    if(createLecture(v)) {
                        Toast.makeText(getApplicationContext(), "The room " + mRoomId + " is reserved successfully from " + mStartTime.getText().toString() + " to " + mEndTime.getText().toString() + "!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    break;
                }
            }
        }
    };

    private boolean createLecture(View v) {
        String lectureName = mName.getText().toString();
        long startTimeStamp = 0;
        long endTimeStamp = 0;

        try {
            String dateToday = Helper.DATE_FORMATTER.format(new Date().getTime());
            Date fullStartDate = (Date) Helper.TIMESTAMP_FORMATTER.parse(dateToday+" "+mStartTime.getText().toString());
            startTimeStamp = fullStartDate.getTime();
            Date fullEndDate = (Date) Helper.TIMESTAMP_FORMATTER.parse(dateToday+" "+mEndTime.getText().toString());
            endTimeStamp = fullEndDate.getTime();
            if(startTimeStamp > endTimeStamp){
                Snackbar.make(v, "The start time cannot be greater than the end time.", Snackbar.LENGTH_LONG).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String personName = SP.getString("username", "Anonymous");
        Lecture newLecture = new Lecture(startTimeStamp,lectureName,endTimeStamp, personName);
        DataService.createLecture(newLecture, mRoomId);
        return true;
    }


    private void initializeScreen() {

        mRoomIdTextView = (TextView) findViewById(R.id.textViewRoomId);
        mStartTime = (EditText) findViewById(R.id.editTextStartTime);
        mName = (EditText) findViewById(R.id.editTextName);
        mEndTime = (EditText) findViewById(R.id.editTextEndTime);
        mMakeReservation = (Button) findViewById(R.id.buttonMakeReservation);

        mStartTime.setOnClickListener(onClickListener);
        mEndTime.setOnClickListener(onClickListener);
        mMakeReservation.setOnClickListener(onClickListener);

        mRoomIdTextView.setText(mRoomId);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDateTimeFields() {
        mStartTime.setInputType(InputType.TYPE_NULL);
        mEndTime.setInputType(InputType.TYPE_NULL);

        Calendar newCalendar = Calendar.getInstance();
        //mStartTime.setText(Helper.TIME_FORMATTER.format(newCalendar.getTime()));
        //mEndTime.setText(Helper.TIME_FORMATTER.format(newCalendar.getTime()));

        mStartTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hour, int min) {
                Calendar newDate = Calendar.getInstance();
                //newDate.set(newDate));
                newDate.set(Calendar.HOUR_OF_DAY,hour);
                newDate.set(Calendar.MINUTE, min);
                mStartTime.setText(Helper.TIME_FORMATTER.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));

        mEndTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hour, int min) {
                Calendar newDate = Calendar.getInstance();
                //newDate.set(newDate));
                newDate.set(Calendar.HOUR_OF_DAY,hour);
                newDate.set(Calendar.MINUTE, min);
                mEndTime.setText(Helper.TIME_FORMATTER.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));
    }

}
