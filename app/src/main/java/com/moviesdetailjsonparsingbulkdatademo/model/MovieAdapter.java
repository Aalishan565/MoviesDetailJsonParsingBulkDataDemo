package com.moviesdetailjsonparsingbulkdatademo.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.moviesdetailjsonparsingbulkdatademo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by aalishan on 7/11/16.
 */

public class MovieAdapter extends ArrayAdapter {
    private List<MovieModel> movieModelList;
    private int resource;
    private LayoutInflater inflater;

    public MovieAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.resource = resource;
        movieModelList = objects;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.tvMovieName = (TextView) convertView.findViewById(R.id.tv_moviename);
            viewHolder.tvTagline = (TextView) convertView.findViewById(R.id.tv_tagline);
            viewHolder.tvYear = (TextView) convertView.findViewById(R.id.tv_year);
            viewHolder.tvDuration = (TextView) convertView.findViewById(R.id.tv_duration);
            viewHolder.tvDirector = (TextView) convertView.findViewById(R.id.tv_director);
            viewHolder.rating = (RatingBar) convertView.findViewById(R.id.ratingbar);
            viewHolder.tvCast = (TextView) convertView.findViewById(R.id.tv_cast);
            viewHolder.tvStory = (TextView) convertView.findViewById(R.id.tv_story);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

        ImageLoader.getInstance().displayImage(movieModelList.get(position).getInage(), viewHolder.ivIcon, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        viewHolder.tvMovieName.setText(movieModelList.get(position).getMovie());
        viewHolder.tvTagline.setText(movieModelList.get(position).getTagline());
        viewHolder.tvYear.setText("Year :" + movieModelList.get(position).getYear());
        viewHolder.tvDuration.setText("Duration :" + movieModelList.get(position).getDuration());
        viewHolder.tvDirector.setText("Director :" + movieModelList.get(position).getDirector());
        viewHolder.rating.setRating(movieModelList.get(position).getRating() / 2);

        StringBuffer sb = new StringBuffer();
        for (MovieModel.Cast cast : movieModelList.get(position).getCastList()) {
            sb.append(cast.getName() + ",");

        }
        viewHolder.tvCast.setText("Cast:" + sb);
        viewHolder.tvStory.setText(movieModelList.get(position).getStory());
        return convertView;
    }

    class ViewHolder {
        private ImageView ivIcon;
        private TextView tvMovieName;
        private TextView tvTagline;
        private TextView tvYear;
        private TextView tvDuration;
        private TextView tvDirector;
        private RatingBar rating;
        private TextView tvCast;
        private TextView tvStory;

    }
}

