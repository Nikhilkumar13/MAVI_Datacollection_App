package datacollection.iitd.mavi.datacollectionmavi.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import datacollection.iitd.mavi.datacollectionmavi.Model.SignBoard;
import datacollection.iitd.mavi.datacollectionmavi.R;

/**.
 * Activities that contain this fragment must implement the
 * {@link PopUpDialogFragment.OnPopUpFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PopUpDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopUpDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SIGNBOARD_KEY= "mysignboard";
    private SignBoard mSignBoard;


    Button mDelete , mOkay;

    private OnPopUpFragmentInteractionListener mListener;

    public PopUpDialogFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PopUpDialogFragment newInstance(SignBoard signBoard) {
        PopUpDialogFragment fragment = new PopUpDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(SIGNBOARD_KEY,signBoard);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           mSignBoard=  (SignBoard) getArguments().getSerializable(SIGNBOARD_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_pop_up_dialog, container, false);
        TextView name = (TextView) v.findViewById(R.id.name_textview);
        ImageView im = (ImageView) v.findViewById(R.id.signboard_image);
        mDelete = (Button) v.findViewById(R.id.delete_button);
        mOkay = (Button) v.findViewById(R.id.okay_button);
       TextView lat= (TextView) v.findViewById(R.id.lat_textview);
       TextView lng= (TextView) v.findViewById(R.id.lng_textview);
       TextView angle= (TextView) v.findViewById(R.id.angle_textview);
       TextView comment= (TextView) v.findViewById(R.id.comment_textview);
        im.setImageDrawable(mSignBoard.getImage(getActivity()));

        name.setText(mSignBoard.getName());
        lng.setText(mSignBoard.getLong());
        angle.setText("Angle :  " + String.valueOf(mSignBoard.getAngle()));
        comment.setText(mSignBoard.getComment());
        lat.setText(mSignBoard.getLat());
        mOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 getDialog().dismiss();
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(getActivity())
                        .setTitle("Title")
                        .setMessage("Do you really want to Delete whatever?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                mListener.onPopUpFragmentInteraction(mSignBoard);
                                getDialog().dismiss();




                            }})
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(SignBoard mSignBoard) {
        if (mListener != null) {
            mListener.onPopUpFragmentInteraction(mSignBoard);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPopUpFragmentInteractionListener) {
            mListener = (OnPopUpFragmentInteractionListener) context;
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
    public interface OnPopUpFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPopUpFragmentInteraction(SignBoard signBoard);
    }
}
