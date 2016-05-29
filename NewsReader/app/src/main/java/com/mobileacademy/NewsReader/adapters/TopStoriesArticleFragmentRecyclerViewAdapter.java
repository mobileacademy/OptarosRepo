package com.mobileacademy.NewsReader.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.fragments.TopStoriesArticleFragment;
import com.mobileacademy.NewsReader.models.Article;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Article} and makes a call to the
 * specified
 */
public class TopStoriesArticleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<TopStoriesArticleFragmentRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Article> mValues;
    private final TopStoriesArticleFragment.OnListFragmentInteractionListener mListener;

    public TopStoriesArticleFragmentRecyclerViewAdapter(ArrayList<Article> items, TopStoriesArticleFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void updateData(ArrayList<Article> data) {
        mValues = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitle.setText(mValues.get(position).getName());

        String timestamp = mValues.get(position).getTime();
        if(timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(Long.parseLong(timestamp));

            holder.mCreatedDate.setText(sdf.format(cal.getTime()));
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onTopStoryArticleSelected(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final TextView mCreatedDate;
        public Article mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.title_tv);
            mCreatedDate = (TextView) view.findViewById(R.id.created_date_tv);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }
}
