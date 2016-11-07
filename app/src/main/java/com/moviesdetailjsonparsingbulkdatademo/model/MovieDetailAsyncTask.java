package com.moviesdetailjsonparsingbulkdatademo.model;

import android.os.AsyncTask;

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

/**
 * Created by aalishan on 7/11/16.
 */

public class MovieDetailAsyncTask extends AsyncTask<String, String, List<MovieModel>> {
    private IMovieListener iMovieListener;
    public final static String MOVIE_LIST_URL = "http://jsonparsing.parseapp.com/jsonData/moviesData.txt";

    public MovieDetailAsyncTask(IMovieListener iMovieListener) {
        this.iMovieListener = iMovieListener;
    }

    @Override
    protected List<MovieModel> doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(MOVIE_LIST_URL);
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
    protected void onPostExecute(List<MovieModel> movieModels) {
        if (!movieModels.isEmpty()) {
            iMovieListener.onMovieDownloadSuccessListener(movieModels);
        } else {
            iMovieListener.onMovieDownloadFailureListener();
        }
    }
}
