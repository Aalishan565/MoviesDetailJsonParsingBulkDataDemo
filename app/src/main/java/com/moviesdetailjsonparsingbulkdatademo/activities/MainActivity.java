package com.moviesdetailjsonparsingbulkdatademo.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.moviesdetailjsonparsingbulkdatademo.R;
import com.moviesdetailjsonparsingbulkdatademo.model.IMovieListener;
import com.moviesdetailjsonparsingbulkdatademo.model.MovieAdapter;
import com.moviesdetailjsonparsingbulkdatademo.model.MovieDetailAsyncTask;
import com.moviesdetailjsonparsingbulkdatademo.model.MovieModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IMovieListener {
    private ListView mLvMoiveDatail;
    private ProgressDialog pd;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        mLvMoiveDatail = (ListView) findViewById(R.id.listView);
        pd = new ProgressDialog(MainActivity.this);
        new MovieDetailAsyncTask(this).execute();
    }


    @Override
    public void onMovieDownloadSuccessListener(List<MovieModel> movieModel) {
        pd.dismiss();
        mMovieAdapter = new MovieAdapter(MainActivity.this, R.layout.row, movieModel);
        mLvMoiveDatail.setAdapter(mMovieAdapter);
    }

    @Override
    public void onMovieDownloadFailureListener() {
        pd.dismiss();

    }
}



