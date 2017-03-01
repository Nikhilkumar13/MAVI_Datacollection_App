package datacollection.iitd.mavi.datacollectionmavi;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DataFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DataFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataFormFragment extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Button mAddMore;
    private static LinearLayout mLayout;
    private int mCount=0;

    private SensorManager mSensorManager;
    private Sensor mCompass;
    private TextView mSenserTextView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DataFormFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DataFormFragment newInstance() {
        DataFormFragment fragment = new DataFormFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.form_fragment,container,false);
        mAddMore = (Button) rootView.findViewById(R.id.add_more_button);
        mLayout= (LinearLayout) rootView.findViewById(R.id.data_container);

        TextView tv= (TextView) mLayout.findViewById(R.id.block_number_text_view);
        tv.setText(getString(R.string.block_number,mCount));
        mSenserTextView= (TextView) rootView.findViewById(R.id.signboard_direction);

        mAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View block = inflater.inflate(R.layout.signboard_data_blocks,null);
                TextView textView = (TextView) block.findViewById(R.id.block_number_text_view);
                textView.setText((getString(R.string.block_number,++mCount)));
                textView.setFocusableInTouchMode(true);
                textView.requestFocus();

                mLayout.addView( block);



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

    @Override
    public void onSensorChanged(SensorEvent event) {

        float azimuth = Math.round(event.values[0]);
        // The other values provided are:
        //  float pitch = event.values[1];
        //  float roll = event.values[2];
        mSenserTextView.setText("Azimuth: " + Float.toString(azimuth));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        // Unregister the listener on the onPause() event to preserve battery life;
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mCompass, SensorManager.SENSOR_DELAY_NORMAL);
    }
}




