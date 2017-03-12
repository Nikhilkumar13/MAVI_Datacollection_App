package datacollection.iitd.mavi.datacollectionmavi.Fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import datacollection.iitd.mavi.datacollectionmavi.Database.MySQLiteHelper;
import datacollection.iitd.mavi.datacollectionmavi.Helper.Constants;
import datacollection.iitd.mavi.datacollectionmavi.Helper.FileUtils;
import datacollection.iitd.mavi.datacollectionmavi.Model.SignBoard;
import datacollection.iitd.mavi.datacollectionmavi.R;

import static android.app.Activity.RESULT_OK;

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
    private TextView mSenserTextView ,mNameEditText ,mAngleEditText, mCommentEditText ,mPlaceEditText, mCategoryEditText;
    private  Button mSaveButton ,mPlacePickerButton;
    //Image properties
    private String mCurrentImagePath = null;
    private Uri mCapturedImageURI = null;
    private ImageButton mImageButton;
    private MySQLiteHelper db;
    private View mRootView;
    private Place mPlace;


    private int PLACE_PICKER_REQUEST = 1;


    private SignBoard mSignboard;


    private void InitializeViews()
    {
        mSenserTextView = (TextView) mRootView.findViewById(R.id.compass_textview);
        mNameEditText = (TextView) mRootView.findViewById(R.id.signboard_name_edittext);
        mCategoryEditText = (TextView) mRootView.findViewById(R.id.category_edittext);
        mAngleEditText= (TextView) mRootView.findViewById(R.id.angle_edittext);
        mCommentEditText= (TextView) mRootView.findViewById(R.id.comment_edittext);
        mPlaceEditText= (TextView) mRootView.findViewById(R.id.place_edittext);
        mImageButton= (ImageButton) mRootView.findViewById(R.id.signboard_image_button);
        mSaveButton = (Button) mRootView.findViewById(R.id.save_button);
        mPlacePickerButton = (Button) mRootView.findViewById(R.id.place_picker_button);
        mPlacePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                }

                catch (Exception e) {
                    e.printStackTrace();

                }
//                catch
//                 (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSignboard();
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });



    }



    // TODO: Rename and change types of parameters
    private String mLat, mLong;

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
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        mSignboard = new SignBoard();
        db = new MySQLiteHelper(getActivity());
        if (savedInstanceState != null) {

            // Get the saved Image uri string.
            String ImageUriString = savedInstanceState.getString(Constants.KEY_IMAGE_URI);

            // Restore the Image uri from the Image uri string.
            if (ImageUriString != null) {
                mCapturedImageURI = Uri.parse(ImageUriString);
            }
            mCurrentImagePath = savedInstanceState.getString(Constants.KEY_IMAGE_URI);
        }

        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.form_fragment, container, false);
        InitializeViews();
        return mRootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCapturedImageURI != null) {
            outState.putString(Constants.KEY_IMAGE_URI, mCapturedImageURI.toString());
        }
        outState.putString(Constants.KEY_IMAGE_PATH, mCurrentImagePath);
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
        void onFragmentInteraction();
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

    private void SaveSignboard(){


        if(!Validate())
        {
            Toast.makeText(getContext(),"Fill the Form Carefully", Toast.LENGTH_SHORT).show();
            return;

        }

        mSignboard.setName(mNameEditText.getText().toString());
        mSignboard.setComment(mCommentEditText.getText().toString());
        mSignboard.setAngle(Integer.valueOf(mAngleEditText.getText().toString()));
        mSignboard.setLat(String.valueOf(mPlace.getLatLng().latitude));
        mSignboard.setLong(String.valueOf(mPlace.getLatLng().longitude));
        mSignboard.setCategory(mCategoryEditText.getText().toString().toLowerCase());
        mSignboard.setIsPushed(false);

        //Check to see if there is valid image path temporarily in memory
        //Then save that image path to the database and that becomes the profile
        //Image for this user.
        if (mCurrentImagePath != null && !mCurrentImagePath.isEmpty())
        {
            mSignboard.setImagePath(mCurrentImagePath);
        }

        long result = db.addSignboard(mSignboard);
        if (result == -1 ){
            Toast.makeText(getActivity(), "Unable to add customer: " + mSignboard.getName(), Toast.LENGTH_LONG).show();
        }
        else
        {

            Toast.makeText(getActivity(), "Succesfully Added "+ mSignboard.getName(), Toast.LENGTH_LONG).show();
            mListener.onFragmentInteraction();

        }
    }

    private void chooseImage(){

        //We need the customer's name to to save the image file
        if (mNameEditText.getText() != null && !mNameEditText.getText().toString().isEmpty()) {
            // Determine Uri of camera image to save.
            final File rootDir = new File(Constants.PICTURE_DIRECTORY);

            //noinspection ResultOfMethodCallIgnored
            rootDir.mkdirs();

            // Create the temporary file and get it's URI.

            //Get the customer name
            String customerName = mNameEditText.getText().toString();

            //Remove all white space in the customer name
            customerName.replaceAll("\\s+", "");

            //Use the customer name to create the file name of the image that will be captured
            File file = new File(rootDir, FileUtils.generateImageName(customerName));
            mCapturedImageURI = Uri.fromFile(file);

            // Initialize a list to hold any camera application intents.
            final List<Intent> cameraIntents = new ArrayList<Intent>();

            // Get the default camera capture intent.
            final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Get the package manager.
            final PackageManager packageManager = getActivity().getPackageManager();

            // Ensure the package manager exists.
            if (packageManager != null) {

                // Get all available image capture app activities.
                final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);

                // Create camera intents for all image capture app activities.
                for(ResolveInfo res : listCam) {

                    // Ensure the activity info exists.
                    if (res.activityInfo != null) {

                        // Get the activity's package name.
                        final String packageName = res.activityInfo.packageName;

                        // Create a new camera intent based on android's default capture intent.
                        final Intent intent = new Intent(captureIntent);

                        // Set the intent data for the current image capture app.
                        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                        intent.setPackage(packageName);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

                        // Add the intent to available camera intents.
                        cameraIntents.add(intent);
                    }
                }
            }

            // Create an intent to get pictures from the filesystem.
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

            // Chooser of filesystem options.
            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

            // Add the camera options.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

            // Start activity to choose or take a picture.
            startActivityForResult(chooserIntent, Constants.ACTION_REQUEST_IMAGE);
        } else {
            mNameEditText.setError("Please enter Signboard name");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                mPlace = PlacePicker.getPlace(getActivity(),data);
                String toastMsg = String.format("Place: %s", mPlace.getName());
                mPlaceEditText.setEllipsize(TextUtils.TruncateAt.END);
                mPlaceEditText.setText(mPlace.getAddress());
                Toast.makeText(getActivity(), mPlace.getAddress(), Toast.LENGTH_LONG).show();
            }
            return ;
        }
        if (resultCode == RESULT_OK){
            // Get the resultant image's URI.
            final Uri selectedImageUri = (data == null) ? mCapturedImageURI : data.getData();

            // Ensure the image exists.
            if (selectedImageUri != null) {

                // Add image to gallery if this is an image captured with the camera
                //Otherwise no need to re-add to the gallery if the image already exists
                if (requestCode == Constants.ACTION_REQUEST_IMAGE) {
                    final Intent mediaScanIntent =
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(selectedImageUri);
                    getActivity().sendBroadcast(mediaScanIntent);
                }

                mCurrentImagePath = FileUtils.getPath(getActivity(), selectedImageUri);

                // Update client's picture
                if (mCurrentImagePath != null && !mCurrentImagePath.isEmpty()) {
                    mImageButton.setImageDrawable(new BitmapDrawable(getResources(),
                            FileUtils.getResizedBitmap(mCurrentImagePath, 512, 512)));
                }
            }
        }


    }


    private Boolean Validate()
    {

        if (mNameEditText.getText() == null || mNameEditText.getText().toString().isEmpty())
        {
            mNameEditText.setError("Provide Name");
            return false;
        }
        if (mAngleEditText.getText() == null || mAngleEditText.getText().toString().isEmpty())
        {
            mAngleEditText.setError("Angle Missing");
            return  false;
        }
        if (mPlaceEditText.getText() == null || mPlaceEditText.getText().toString().isEmpty())
        {
            mPlaceEditText.setError("Choose a Place");
            return  false;

        }
        return true;

    }





}




