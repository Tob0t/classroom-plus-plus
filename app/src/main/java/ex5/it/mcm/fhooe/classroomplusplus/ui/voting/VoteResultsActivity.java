package ex5.it.mcm.fhooe.classroomplusplus.ui.voting;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import ex5.it.mcm.fhooe.classroomplusplus.R;
import ex5.it.mcm.fhooe.classroomplusplus.model.Survey;
import ex5.it.mcm.fhooe.classroomplusplus.model.Vote;
import ex5.it.mcm.fhooe.classroomplusplus.utils.Constants;

public class VoteResultsActivity extends AppCompatActivity {

    private static final String LOG_TAG = VoteResultsActivity.class.getSimpleName();

    private Query mSurveyRef;
    private ValueEventListener mSurveyRefListener;
    private Survey mSurvey;
    private TextView mQuestionTxt, mLeftAnswerTxt, mRightAnswerTxt;

    private ImageView mLeftImg, mRightImg;
    private String mSurveyId;

    // Temp
    private Firebase mVoteRef;
    private ValueEventListener mVoteRefListener;
    private TextView mResultRightTxt, mResultLeftTxt;
    private int mLeftCounter, mRightCounter;

    private PieChart mChart;
    private PieData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_results);

        // inital values
        mLeftCounter = 0;
        mRightCounter = 0;


         /* Get the push ID from the extra passed by ShoppingListFragment */
        Intent intent = this.getIntent();
        mSurveyId = intent.getStringExtra(Constants.KEY_SURVEY_ID);

        initializeScreen();

        /**
         * Create Firebase references
         */
        mSurveyRef = new Firebase(Constants.FIREBASE_URL_SURVEYS).child(mSurveyId);
        mVoteRef = new Firebase(Constants.FIREBASE_URL_VOTES).child(mSurveyId);

        mSurveyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSurvey = dataSnapshot.getValue(Survey.class);
                if(mSurvey != null) {
                    mQuestionTxt.setText(mSurvey.getQuestion());
                    setupChart(mSurvey.getLeftAnswer(), mSurvey.getRightAnswer());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mVoteRefListener = mVoteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    // init counters to zero
                    int leftCounter = 0;
                    int rightCounter = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Vote mVote = snapshot.getValue(Vote.class);
                        if (mVote != null) {
                            if (mVote.isLeftAnswer()) {
                                leftCounter++;
                            } else if (mVote.isRightAnswer()) {
                                rightCounter++;
                            }
                        }
                    }
                    // only update if there is a change
                    if(leftCounter != mLeftCounter || rightCounter != mRightCounter) {
                        updateData(leftCounter, rightCounter);
                        mLeftCounter = leftCounter;
                        mRightCounter = rightCounter;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }

    private void setupChart(String leftAnswer, String rightAnswer){

        mChart.setUsePercentValues(true);
        mChart.setDescription("Survey results");
        mChart.setDrawSliceText(true);

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add(leftAnswer);
        xVals.add(rightAnswer);

        // instantiate pie data object now
        ArrayList<Entry> vals = new ArrayList<Entry>();
        PieDataSet dataSet = new PieDataSet(vals, "");

        mData = new PieData(xVals, dataSet);
        mChart.setData(mData);

    }

    private void updateData(int leftCounter, int rightCounter){

        // remove old dataset
        if(mData != null) {
            mData.removeDataSet(0);
        }

        // add new values
        ArrayList<Entry> vals = new ArrayList<Entry>();
        vals.add(new Entry(leftCounter,0));
        vals.add(new Entry(rightCounter,1));

        PieDataSet dataSet = new PieDataSet(vals, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        mData.addDataSet(dataSet);
        mData.setValueFormatter(new PercentFormatter());
        mData.setValueTextSize(20f);

        mChart.notifyDataSetChanged(); // let the chart know it's data changed
        //mChart.invalidate(); // refresh
        mChart.animateXY(2000,2000);

    }


    private void setupChartBarChart(){
        ArrayList<BarEntry> vals = new ArrayList<BarEntry>();

        BarDataSet dataSet = new BarDataSet(vals, "Answers");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("LeftAnswer");
        xVals.add("RightAnswer");

        /*mData = new BarData(xVals, dataSet);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);*/

        mChart.setData(mData);
    }

    private void updateDataBarChar(int mLeftCounter, int mRightCounter){
        // remove old dataset
        mData.removeDataSet(0);

        // add new values
        ArrayList<BarEntry> vals = new ArrayList<BarEntry>();
        vals.add(new BarEntry(mLeftCounter,0));
        vals.add(new BarEntry(mRightCounter,1));

        BarDataSet dataSet = new BarDataSet(vals, "Answers");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //mData.addDataSet(dataSet);

        mChart.animateY(2000);
        mChart.notifyDataSetChanged(); // let the chart know it's data changed
        mChart.invalidate(); // refresh
    }

    private void initializeScreen() {
        mQuestionTxt =  (TextView) findViewById(R.id.txtQuestion);
        mChart = (PieChart) findViewById(R.id.chart);

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
        mVoteRef.removeEventListener(mVoteRefListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
