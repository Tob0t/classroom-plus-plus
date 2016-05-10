package ex5.it.mcm.fhooe.classroomplusplus.ui.availability;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;

import ex5.it.mcm.fhooe.classroomplusplus.R;
import ex5.it.mcm.fhooe.classroomplusplus.model.Lecture;
import ex5.it.mcm.fhooe.classroomplusplus.utils.Constants;
import me.grantland.widget.AutofitHelper;

public class AvailabilityResultsActivity extends AppCompatActivity {
    private TextView mRoomNumberTxt, mStateTxt, mCurrentLectureTxt, mTimeOccupiedTxt, mTimeNextLectureTxt;
    private TextView mOccupiedTxt;
    private Button mOccupyButton;
    private ImageView mStateImage;

    private String mRoomId;

    private Query mLectureRef;
    private ValueEventListener mLectureRefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_results);

         /* Get the push ID from the extra passed by ShoppingListFragment */
        Intent intent = this.getIntent();
        mRoomId = intent.getStringExtra(Constants.KEY_ROOM_ID);

        initializeScreen();

        final ProgressDialog  progress = ProgressDialog.show(this, "Please Wait ...",
                "Checking room state ..", true);

        /**
         * Create Firebase references
         */
        mLectureRef = new Firebase(Constants.FIREBASE_URL_LECTURES).child(mRoomId).orderByChild("startTime");
        mLectureRefListener = mLectureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showAvailability(dataSnapshot);
                progress.dismiss();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void showAvailability(DataSnapshot dataSnapshot) {
        Constants.RoomStatus roomStatus = Constants.RoomStatus.FREE;;
        long currentTs = System.currentTimeMillis();
        roomStatus = Constants.RoomStatus.FREE;
        for (DataSnapshot lectureSnapshot: dataSnapshot.getChildren()) {
            Lecture lecture = lectureSnapshot.getValue(Lecture.class);
            if(lecture.getStartTime() < currentTs && lecture.getEndTime() > currentTs){
                roomStatus = Constants.RoomStatus.OCCUPIED;
                mStateTxt.setText("Occupied");
                mCurrentLectureTxt.setText(lecture.getName());
                String lectureEndTime = new SimpleDateFormat("MM.dd HH:mm").format(lecture.getEndTime());
                mTimeOccupiedTxt.setText(lectureEndTime);
                mStateImage.setImageResource(R.drawable.occupied);
                mStateImage.setVisibility(View.VISIBLE);
            }
            if(lecture.getStartTime() > currentTs){
                String nextLectureStartTime = new SimpleDateFormat("MM.dd HH:mm").format(lecture.getStartTime());
                mTimeNextLectureTxt.setText(nextLectureStartTime);
                // jump out of the loop since we only need the first occurence
                break;
            }
        }
        // if the room status is still free at the end of the loop than show button occupy
        if(roomStatus == Constants.RoomStatus.FREE){
            mStateTxt.setText("Free");
            mStateImage.setImageResource(R.drawable.free);
            mOccupyButton.setVisibility(View.VISIBLE);
        }
        mStateImage.setVisibility(View.VISIBLE);
    }

    private void initializeScreen() {
        mRoomNumberTxt =  (TextView) findViewById(R.id.txtRoomNumber);
        mStateTxt =  (TextView) findViewById(R.id.txtState);
        mCurrentLectureTxt =  (TextView) findViewById(R.id.txtLectureName);
        mTimeOccupiedTxt =  (TextView) findViewById(R.id.txtTimeOccupied);
        mTimeNextLectureTxt =  (TextView) findViewById(R.id.txtTimeNextLecture);

        mOccupiedTxt =  (TextView) findViewById(R.id.txtOccupied);
        mOccupyButton = (Button) findViewById(R.id.buttonOccupy);

        mStateImage = (ImageView) findViewById(R.id.imageViewState);

        mRoomNumberTxt.setText(mRoomId);
        setTitle(mRoomId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLectureRef.removeEventListener(mLectureRefListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
