package com.moviesdetailjsonparsingbulkdatademo.model;

import java.util.List;

/**
 * Created by aalishan on 7/11/16.
 */

public interface IMovieListener {
    void onMovieDownloadSuccessListener(List<MovieModel> movieModel);

    void onMovieDownloadFailureListener();
}
