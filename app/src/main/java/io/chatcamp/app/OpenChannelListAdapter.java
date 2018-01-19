package io.chatcamp.app;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;

import io.chatcamp.sdk.OpenChannel;

import java.util.List;

/**
 * Created by ChatCamp Team on 29/11/17.
 */

public class OpenChannelListAdapter extends RecyclerView.Adapter<OpenChannelListAdapter.ViewHolder> {

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    private List<OpenChannel> mDataset;
    private RecyclerViewClickListener mListener;
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        private RecyclerViewClickListener mListener;
        public View mTextView;
        public ViewHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            mTextView = v;
            mListener = listener;
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition()); // call the onClick in the OnItemClickListener
        }
    }

    public OpenChannelListAdapter(List<OpenChannel> myDataset, RecyclerViewClickListener listener) {
        mDataset = myDataset;
        mListener = listener;
    }

    @Override
    public OpenChannelListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_channel_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView mText = (TextView) holder.mTextView.findViewById(R.id.firstLine);
        mText.setText(mDataset.get(position).getName());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}






