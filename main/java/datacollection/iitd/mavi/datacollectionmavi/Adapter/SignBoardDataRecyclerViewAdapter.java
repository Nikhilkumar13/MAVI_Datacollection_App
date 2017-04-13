package datacollection.iitd.mavi.datacollectionmavi.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import datacollection.iitd.mavi.datacollectionmavi.Fragment.DataListFragment.OnListFragmentInteractionListener;
import datacollection.iitd.mavi.datacollectionmavi.Fragment.PopUpDialogFragment;
import datacollection.iitd.mavi.datacollectionmavi.Helper.FileUtils;
import datacollection.iitd.mavi.datacollectionmavi.Model.SignBoard;
import datacollection.iitd.mavi.datacollectionmavi.R;

import java.lang.ref.WeakReference;
import java.util.List;



public class SignBoardDataRecyclerViewAdapter extends RecyclerView.Adapter<SignBoardDataRecyclerViewAdapter.ViewHolder> {

    private static final String TAG =  "SIGNBOARDADAAPTER";
    private final LruCache<String, Bitmap> mLruCache;
    private  List<SignBoard> mSignboard;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;

    public SignBoardDataRecyclerViewAdapter(Context context ,List<SignBoard> items, OnListFragmentInteractionListener listener) {
        mSignboard = items;
        mListener = listener;
        mContext=context;

        //Find out maximum memory available to application
        //1024 is used because LruCache constructor takes int in kilobytes
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/4th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 4;
        Log.d(TAG, "max memory " + maxMemory + " cache size " + cacheSize);

        // LruCache takes key-value pair in constructor
        // key is the string to refer bitmap
        // value is the stored bitmap
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_data, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SignBoard sb= mSignboard.get(position);


        Bitmap thumbnailImage = null;
        String imageKey=sb.getImagePath();
        thumbnailImage = getBitmapFromMemCache(imageKey);

        if (thumbnailImage == null){
            // if asked thumbnail is not present it will be put into cache
            BitmapWorkerTask task = new BitmapWorkerTask(holder.Thumbnail);
            task.execute(imageKey);
        }



        holder.Name.setText(sb.getName());
        holder.Comment.setText(sb.getComment());
        holder.Thumbnail.setImageBitmap(thumbnailImage);
        holder.mView.setTag(position);
        if(sb.getIsPushed()==1)
        {
//            Drawable dr= get
            holder.Sign.setImageResource(R.drawable.check);

        }
        else
        {
            holder.Sign.setImageResource(R.drawable.close);


        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //                if (null != mListener) {.
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(mSignboard.get(Integer.valueOf(v.getTag().toString())));
//                }

//                Toast.makeText(mContext,"Clicked" +String.valueOf(),Toast.LENGTH_SHORT).show();


//
//                /** Getting the previously created fragment object from the fragment manager */
//                TimeDialogFragment tPrev =  ( TimeDialogFragment ) fragmentManager.findFragmentByTag("time_dialog");
//
//                /** If the previously created fragment object still exists, then that has to be removed */
//                if(tPrev!=null)
//                    fragmentTransaction.remove(tPrev);
//
//                /** Opening the fragment object */
//                tFragment.show(fragmentTransaction, "time_dialog");
            }
        });

            }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mLruCache.get(key);
    }




    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return mSignboard.size();
    }

    public void  setData(List<SignBoard >data)
    {
        mSignboard=data;
        this.notifyDataSetChanged();
    }
    public void Update()
    {
        mSignboard.clear();
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final  View mView;
        private TextView Name;
        private TextView Comment;
        private ImageView Thumbnail;
        private ImageView Sign;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            Name = (TextView) view.findViewById(R.id.signboard_name_textview);
            Comment = (TextView) view.findViewById(R.id.signboard_comment_textview);
            Thumbnail = (ImageView) view.findViewById(R.id.signboard_image_thumbnail);
            Sign= (ImageView) view.findViewById(R.id.tick_or_cross_image);
        }


    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            final Bitmap bitmap = FileUtils.getResizedBitmap(params[0],128,128);
            addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
            return bitmap;
        }

        //  onPostExecute() sets the bitmap fetched by doInBackground();
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
