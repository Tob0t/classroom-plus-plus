package ex5.it.mcm.fhooe.classroomplusplus.ui.voting;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import ex5.it.mcm.fhooe.classroomplusplus.R;
import ex5.it.mcm.fhooe.classroomplusplus.model.Survey;
import ex5.it.mcm.fhooe.classroomplusplus.ui.OnFragmentInteractionListener;
import ex5.it.mcm.fhooe.classroomplusplus.utils.Constants;
import ex5.it.mcm.fhooe.classroomplusplus.utils.DataService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoteFragment extends Fragment {

    private static final String LOG_TAG = VoteFragment.class.getSimpleName();

    private Query mSurveyRef;
    private ChildEventListener mSurveyRefListener;
    private Survey mSurvey;
    private TextView mQuestionTxt, mLeftAnswerTxt, mRightAnswerTxt;

    private ImageView mBgImg;
    private String mSurveyId;


    private OnFragmentInteractionListener mListener;

    public VoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VoteFragment.
     */
    public static VoteFragment newInstance() {
        VoteFragment fragment = new VoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_vote, container, false);

        initializeScreen(rootView);

        final ProgressDialog progress = ProgressDialog.show(getActivity(), "Please Wait ...",
                "Collecting current survey ..", true);

        /**
         * Create Firebase references
         */
        mSurveyRef = new Firebase(Constants.FIREBASE_URL_SURVEYS).limitToLast(1);

        mSurveyRefListener = mSurveyRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mSurvey = dataSnapshot.getValue(Survey.class);
                mSurveyId = dataSnapshot.getKey();
                if(mSurvey != null) {
                    mQuestionTxt.setText(mSurvey.getQuestion());
                    mLeftAnswerTxt.setText(mSurvey.getLeftAnswer());
                    mRightAnswerTxt.setText(mSurvey.getRightAnswer());
                }
                progress.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mSurvey = dataSnapshot.getValue(Survey.class);
                if(mSurvey != null) {
                    mQuestionTxt.setText(mSurvey.getQuestion());
                    mLeftAnswerTxt.setText(mSurvey.getLeftAnswer());
                    mRightAnswerTxt.setText(mSurvey.getRightAnswer());
                }
                progress.dismiss();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        // Hide progress bar after 10 seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (progress.isShowing()) {
                    progress.dismiss();
                    Snackbar.make(rootView, "No survey found, please ensure that a survey is created!", Snackbar.LENGTH_LONG).show();
                }
            }
        };
        handler.postDelayed(runnable, 10000);

        return rootView;
    }

    private void initializeScreen(View rootView) {

        mQuestionTxt =  (TextView) rootView.findViewById(R.id.txtQuestion);
        mLeftAnswerTxt =  (TextView) rootView.findViewById(R.id.txtLeftAnswer);
        mRightAnswerTxt =  (TextView) rootView.findViewById(R.id.txtRightAnswer);

        mBgImg = (ImageView) rootView.findViewById(R.id.bgImage);

        mBgImg.setOnClickListener(onClickListener);
        mLeftAnswerTxt.setOnClickListener(onClickListener);
        mRightAnswerTxt.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSurveyId != null) {
                switch (v.getId()) {
                    case R.id.txtLeftAnswer: {
                        handleAction(Constants.NFC_ID, Constants.Answer.LEFT, mSurveyId);
                        break;
                    }
                    case R.id.txtRightAnswer: {
                        handleAction(Constants.NFC_ID, Constants.Answer.RIGHT, mSurveyId);
                        break;
                    }
                }
            }
        }
    };

    public void handleAction(String voteId, Constants.Answer answer, String surveyId) {
        DataService.sendAnswer(answer, voteId, surveyId);

        Toast.makeText(getActivity().getApplicationContext(),"Sending mock data answer "+answer+"..",Toast.LENGTH_LONG).show();
        // send SurveyId to Activity
        Intent intent = new Intent(getActivity().getApplicationContext(), VoteResultsActivity.class);
        intent.putExtra(Constants.KEY_SURVEY_ID, surveyId);
        startActivity(intent);
    }


    @Override
    public void onPause() {
        super.onPause();
        mSurveyRef.removeEventListener(mSurveyRefListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSurveyRef.addChildEventListener(mSurveyRefListener);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

