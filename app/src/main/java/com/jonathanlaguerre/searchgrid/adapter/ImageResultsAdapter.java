package com.jonathanlaguerre.searchgrid.adapter;

/**
 * Created by jonathanlaguerre on 7/9/15.
 */

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonathanlaguerre.searchgrid.R;
import com.jonathanlaguerre.searchgrid.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

        public ImageResultsAdapter(Context context, List<ImageResult> images){
             super(context, R.layout.item_image_result,images );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ImageResult imageInfo = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
            }
            ImageView ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
            //TextView tvTitle = (TextView)convertView.findViewById(R.id.ivTitle);
            //clear image
            ivImage.setImageResource(0);
            //Populate title
           // tvTitle.setText(Html.fromHtml(imageInfo.title));
            //Download image
            Picasso.with(getContext()).load(imageInfo.thumbUrl).into(ivImage);
            //return complete view to be displayed
            return convertView;
        }
}
