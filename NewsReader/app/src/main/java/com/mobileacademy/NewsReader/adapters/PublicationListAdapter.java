package com.mobileacademy.NewsReader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileacademy.NewsReader.models.Publication;
import com.mobileacademy.NewsReader.R;

import java.util.ArrayList;

/**
 * Created by danielastamati on 15/04/16.
 */
public class PublicationListAdapter extends BaseAdapter {

    ArrayList<Publication> list;
    LayoutInflater inflater;

    public PublicationListAdapter(Context context, ArrayList<Publication> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Gets a View that displays the data at the specified position in the data set.
     * You can either create a View manually or inflate it from an XML layout file.
     *
     * @param position
     * @param convertView - the old view to reuse (if it exists)
     * @param parent - the parent to which the view is attached to
     * @return - a view to add in the GridView
     *
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Publication article = (Publication) getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_publication, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(article.getName());
        viewHolder.imageView.setImageResource(article.getPictureResource());

        return convertView;
    }

    /**
     * View Holder class to hold refferences to views inside grid items
     */
    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}
