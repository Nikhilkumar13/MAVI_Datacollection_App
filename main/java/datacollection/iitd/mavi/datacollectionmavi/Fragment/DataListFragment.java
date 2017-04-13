package datacollection.iitd.mavi.datacollectionmavi.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import datacollection.iitd.mavi.datacollectionmavi.Database.MySQLiteHelper;
import datacollection.iitd.mavi.datacollectionmavi.Helper.Constants;
import datacollection.iitd.mavi.datacollectionmavi.Helper.FileUtils;
import datacollection.iitd.mavi.datacollectionmavi.Model.SignBoard;
import datacollection.iitd.mavi.datacollectionmavi.R;
import datacollection.iitd.mavi.datacollectionmavi.Adapter.SignBoardDataRecyclerViewAdapter;
import datacollection.iitd.mavi.datacollectionmavi.dummy.DummyContent;
import datacollection.iitd.mavi.datacollectionmavi.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DataListFragment extends Fragment  {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private String TAG="DATA List Fragment";


    private List<SignBoard> mSignboard;
    private MySQLiteHelper db;

    private SignBoardDataRecyclerViewAdapter mAdapter;
    private boolean mIsLock=false;
    private static  RequestQueue mQueue  ;
    ArrayList<String > mSuccesIds;





    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DataListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DataListFragment newInstance() {
        DataListFragment fragment = new DataListFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=  new MySQLiteHelper(getActivity());
        mQueue= Volley.newRequestQueue(getContext());


//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_list, container, false);

        mSignboard = new ArrayList();
        mSignboard = db.getAllCustomers();


        mAdapter = new SignBoardDataRecyclerViewAdapter(getContext(),mSignboard, mListener);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void reLoadListData()
    {
        mSignboard = db.getAllCustomers();
        mAdapter.setData(mSignboard);
        mAdapter.notifyDataSetChanged();

    }

    public  void  deleteSignboard( long id)
    {
        db.deleteSignboard(id);
        reLoadListData();


    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(SignBoard item);
    }


    public void pushData()
    {

        Toast.makeText(getContext(),"Trying to Push UnPushed Data to Server",Toast.LENGTH_LONG).show();
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());



        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
                @Override
                public void run() {
                    mIsLock = true;
                    String ip=pref.getString("ip","Ip Not Set");
//                    isConnection(ip,80);
                    //Check wheather connection is up or not.

                    if(true) {

                    mSuccesIds = new ArrayList<>();

                    List<SignBoard> signBoards = db.getUnPushedCustomers();

                    String url = "http://" + pref.getString("ip", "Ip not set") + Constants.DATA_URL;
                    String token = pref.getString("token", "tokennoteset");
                    boolean delete_pref = pref.getBoolean("deletelocal", false);


                    for (SignBoard signBoard : signBoards) {
                        mQueue.add(createRequestObject(signBoard, url, token, delete_pref));
//                    Log.d(TAG,String.valueOf(signBoard.getIsPushed()));
//                    db.setPushedTrue(signBoard.getId());

                    }
//                mSuccesIds=null;


                    mIsLock = false;
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Unable to reach Server at "+ip,Toast.LENGTH_LONG).show();
                    }


                }

            });




    }
    public JsonObjectRequest createRequestObject(final SignBoard sb , String URL, final String token, final boolean pref) {


//        Map<String, String> params = new HashMap<String, String>();
        JSONObject params= new JSONObject();
        try {

            params.put("name", sb.getName());
            params.put("angle", String.valueOf(sb.getAngle()));
            params.put("radius", String.valueOf(sb.getAngle()));

            JSONObject location = new JSONObject();
            location.put("lat", String.valueOf(sb.getLat()));
            location.put("lon", String.valueOf(sb.getLong()));
            params.put("location", location);

            String[] category_tags = sb.getCategory().split(",");

            params.put("category_tags", new JSONArray(category_tags));
            String [] data = {};
            params.put("data", new JSONArray(data));
            params.put("image", FileUtils.getImageToBase64(sb.getImagePath()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
//        params.put("image","somrandomstring");
//        Log.d("Our object", params.toString());


//        String url= "http://"+ + Constants.DATA_URL;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

//                showProgress(false);

                boolean success = true;


                if (success) {


//                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    SharedPreferences.Editor editor = pref.edit();
                    //Youmay check if user has asked to delete the local Stored data after pushed to Server in prefrence.
                    //Update Flag or Data Accordingly.
                    Log.d(TAG, "Doing Good");
//                    mSuccesIds.add(String.valueOf(sb.getId()));
                    if(pref)
                    {
                        db.deleteSignboard(sb.getId());
                        Log.d(TAG,"Deleted the record");


                    }
                    else
                    {
                        db.setPushedTrue(sb.getId());
                        Log.d(TAG,"updated the record");

                    }




                } else {
                    //SHow toast Message to User that failed to post

                }


            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error", "Error: " + error.getMessage());
                System.out.print(error.getMessage());
                String json = null;

                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 400:
                            json = new String(response.data);
                            json = trimMessage(json, "message");
                            if(json != null) displayMessage(json);
                            break;
                    }
                    //Additional cases
                }


            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("Authorization", "Token "+token);
//                params.put("Content-Type", "application/json");
                return params;
            }
        }


                ;
        return jsonObjectRequest;


    }

    public String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    public void displayMessage(String toastString){
        Toast.makeText(getContext(), toastString, Toast.LENGTH_LONG).show();
    }

    public  boolean isConnection(String ip,int port)
    {
        boolean exists = false;

        try {
            SocketAddress sockaddr = new InetSocketAddress(ip, port);
            // Create an unbound socket
            Socket sock = new Socket();

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = 2000;   // 2 seconds
            sock.connect(sockaddr, timeoutMs);
            exists = true;
        }catch(Exception e){
            e.printStackTrace();
        }

        return exists;
    }
}
