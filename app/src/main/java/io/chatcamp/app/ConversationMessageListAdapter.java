package io.chatcamp.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.chatcamp.sdk.Message;

import java.util.List;

/**
 * Created by ChatCamp Team on 05/12/17.
 */

public class ConversationMessageListAdapter extends RecyclerView.Adapter<ConversationMessageListAdapter.ViewHolder> {
    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }

    private List<Message> mDataset;
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

    public ConversationMessageListAdapter(List<Message> myDataset, RecyclerViewClickListener listener) {
        mDataset = myDataset;
        mListener = listener;
    }

    @Override
    public ConversationMessageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_conversation, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView mText = (TextView) holder.mTextView.findViewById(R.id.tv_message);
        TextView mSender = (TextView) holder.mTextView.findViewById(R.id.tv_sender);
        mText.setText(mDataset.get(position).getText());
        mSender.setText(mDataset.get(position).getUser().getDisplayName());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
