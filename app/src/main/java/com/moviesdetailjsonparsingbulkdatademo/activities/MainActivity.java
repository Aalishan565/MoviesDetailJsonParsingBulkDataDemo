package com.moviesdetailjsonparsingbulkdatademo.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.moviesdetailjsonparsingbulkdatademo.R;
import com.moviesdetailjsonparsingbulkdatademo.model.MovieModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listViewData;
    HttpURLConnection connection = null;
    BufferedReader reader = null;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config); // Do it on Application start
        listViewData = (ListView) findViewById(R.id.listView);
        pd = new ProgressDialog(MainActivity.this);
        new MyAsyncTask().execute();
    }

    private class MyAsyncTask extends AsyncTask<String, String, List<MovieModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("loading");
            pd.show();
        }

        @Override
        protected List<MovieModel> doInBackground(String... params) {
            try {
                URL url = new URL("http://jsonparsing.parseapp.com/jsonData/moviesData.txt");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalData = buffer.toString();
                JSONObject jsonObject = new JSONObject(finalData);
                JSONArray jsonArray = jsonObject.getJSONArray("movies");
                List<MovieModel> movieModelList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonFinalObject = jsonArray.getJSONObject(i);
                    MovieModel movieModel = new MovieModel();
                    movieModel.setMovie(jsonFinalObject.getString("movie"));
                    movieModel.setYear(jsonFinalObject.getInt("year"));
                    movieModel.setRating((float) jsonFinalObject.getDouble("rating"));
                    movieModel.setDirector(jsonFinalObject.getString("director"));
                    movieModel.setDuration(jsonFinalObject.getString("duration"));
                    movieModel.setTagline(jsonFinalObject.getString("tagline"));
                    movieModel.setInage(jsonFinalObject.getString("image"));
                    movieModel.setStory(jsonFinalObject.getString("story"));
                    List<MovieModel.Cast> castList = new ArrayList<>();
                    for (int j = 0; j < jsonFinalObject.getJSONArray("cast").length(); j++) {
                        JSONObject castObject = jsonFinalObject.getJSONArray("cast").getJSONObject(j);
                        MovieModel.Cast cast = new MovieModel.Cast();
                        cast.setName(castObject.getString("name"));
                        castList.add(cast);
                    }
                    movieModel.setCastList(castList);
                    movieModelList.add(movieModel);
                }
                return movieModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(List<MovieModel> s) {
            super.onPostExecute(s);
            MovieAdapter movieAdapter = new MovieAdapter(getApplicationContext(), R.layout.row, s);
            listViewData.setAdapter(movieAdapter);
            pd.hide();
        }
    }


    private class MovieAdapter extends ArrayAdapter {
        private List<MovieModel> movieModelList;
        private int resource;
        LayoutInflater inflater;

        public MovieAdapter(Context context, int resource, List objects) {
            super(context, resource, objects);
            this.resource = resource;
            movieModelList = objects;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
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
}