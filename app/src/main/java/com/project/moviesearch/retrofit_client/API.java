package com.project.moviesearch.retrofit_client;

import com.project.moviesearch.pojo.Movie;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface API {

    @GET("?apikey=7bcaf695&plot=full")
    Call<Movie> getMovie(@Query("t") String movieName);
}
