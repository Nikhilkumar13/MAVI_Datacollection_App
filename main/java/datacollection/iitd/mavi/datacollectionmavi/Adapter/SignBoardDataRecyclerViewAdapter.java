package datacollection.iitd.mavi.datacollectionmavi.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import datacollection.iitd.mavi.datacollectionmavi.Fragment.DataListFragment.OnListFragmentInteractionListener;
import datacollection.iitd.mavi.datacollectionmavi.Model.SignBoard;
import datacollection.iitd.mavi.datacollectionmavi.R;

import java.util.List;



public class SignBoardDataRecyclerViewAdapter extends RecyclerView.Adapter<SignBoardDataRecyclerViewAdapter.ViewHolder> {

    private  List<SignBoard> mSignboard;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;

    public SignBoardDataRecyclerViewAdapter(Context context ,List<SignBoard> items, OnListFragmentInteractionListener listener) {
        mSignboard = items;
        mListener = listener;
        mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        SignBoard sb= mSignboard.get(position);
        holder.Name.setText(sb.getName());
        holder.Comment.setText(sb.getComment());
        holder.Thumbnail.setImageDrawable(sb.getThumbnail(mContext));

//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction();
//                }
//            }
//        });
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

        public ViewHolder(View view) {
            super(view);
            mView = view;
            Name = (TextView) view.findViewById(R.id.signboard_name_textview);
            Comment = (TextView) view.findViewById(R.id.signboard_comment_textview);
            Thumbnail = (ImageView) view.findViewById(R.id.signboard_image_thumbnail);
        }


    }
}
