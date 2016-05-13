package ex5.it.mcm.fhooe.classroomplusplus.ui.availability;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import ex5.it.mcm.fhooe.classroomplusplus.R;
import ex5.it.mcm.fhooe.classroomplusplus.ui.OnFragmentInteractionListener;
import ex5.it.mcm.fhooe.classroomplusplus.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AvailabilityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailabilityFragment extends Fragment {
    private static final String LOG_TAG = AvailabilityFragment.class.getSimpleName();

    private ImageView mImageViewNfcScanner;

    private OnFragmentInteractionListener mListener;

    public AvailabilityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AvailabilityFragment.
     */

    public static AvailabilityFragment newInstance() {
        AvailabilityFragment fragment = new AvailabilityFragment();

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
        final View rootView = inflater.inflate(R.layout.fragment_availability, container, false);

        initializeScreen(rootView);

        return rootView;
    }

    private void initializeScreen(View rootView) {
        mImageViewNfcScanner = (ImageView) rootView.findViewById(R.id.imageViewNfcScanner);
        // OnClickhandler for mockdata
        mImageViewNfcScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"Sending mock data for room "+Constants.MOCK_ROOM_ID+"..",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), AvailabilityResultsActivity.class);
                intent.putExtra(Constants.KEY_ROOM_ID, Constants.MOCK_ROOM_ID);
                startActivity(intent);
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
