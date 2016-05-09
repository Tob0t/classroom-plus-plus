package ex5.it.mcm.fhooe.classroomplusplus.ui.voting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import ex5.it.mcm.fhooe.classroomplusplus.R;
import ex5.it.mcm.fhooe.classroomplusplus.model.Survey;
import ex5.it.mcm.fhooe.classroomplusplus.model.Vote;
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

    private ImageView mLeftImg, mRightImg;
    private String mSurveyId;

    // Mockup ID
    private String NFC_ID = "123456789";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public VoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VoteFragment newInstance(String param1, String param2) {
        VoteFragment fragment = new VoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_vote, container, false);

        initializeScreen(rootView);

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
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mSurvey = dataSnapshot.getValue(Survey.class);
                if(mSurvey != null) {
                    mQuestionTxt.setText(mSurvey.getQuestion());
                    mLeftAnswerTxt.setText(mSurvey.getLeftAnswer());
                    mRightAnswerTxt.setText(mSurvey.getRightAnswer());
                }
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

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void initializeScreen(View rootView) {

        mQuestionTxt =  (TextView) rootView.findViewById(R.id.txtQuestion);
        mLeftAnswerTxt =  (TextView) rootView.findViewById(R.id.txtLeftAnswer);
        mRightAnswerTxt =  (TextView) rootView.findViewById(R.id.txtRightAnswer);

        mLeftImg = (ImageView) rootView.findViewById(R.id.imageViewLeft);
        mRightImg = (ImageView) rootView.findViewById(R.id.imageViewRight);

        mLeftImg.setOnClickListener(onClickListener);
        mRightImg.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mSurveyId != null) {
                // send SurveyId to Activity
                Intent intent = new Intent(getActivity().getApplicationContext(), VoteResultsActivity.class);
                intent.putExtra(Constants.KEY_SURVEY_ID, mSurveyId);
                switch (v.getId()) {
                    case R.id.imageViewLeft: {
                        DataService.sendAnswer(Constants.Answer.LEFT, NFC_ID, mSurveyId);
                        startActivity(intent);
                        break;
                    }
                    case R.id.imageViewRight: {
                        DataService.sendAnswer(Constants.Answer.RIGHT, NFC_ID, mSurveyId);
                        startActivity(intent);
                        break;
                    }
                }
            }
        }
    };

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
