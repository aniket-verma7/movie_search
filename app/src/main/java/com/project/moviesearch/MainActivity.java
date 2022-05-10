package com.project.moviesearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.project.moviesearch.constant.URLConstant;
import com.project.moviesearch.pojo.Movie;
import com.project.moviesearch.retrofit_client.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView title;
    private TextView year;
    private TextView duration;
    private TextView actor;
    private TextView genre;
    private TextView language;
    private TextView plot;
    private TextView imdb;
    private TextView placeHolder;

    private ImageView movieImg;
    private LinearLayout mainLayout;
    private EditText movieName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


    }

    private void init() {
        title = findViewById(R.id.title);
        year = findViewById(R.id.year);
        duration = findViewById(R.id.duration);
        actor = findViewById(R.id.actor);
        genre = findViewById(R.id.genre);
        language = findViewById(R.id.langauge);
        plot = findViewById(R.id.plot);
        imdb = findViewById(R.id.rating);
        placeHolder = findViewById(R.id.placeholder);

        movieImg = findViewById(R.id.moiveImg);
        mainLayout = findViewById(R.id.mainLayout);
        movieName = findViewById(R.id.movieName);

    }


    public void fetchMovie(View view) {

        String inputName = movieName.getText().toString();

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        if (inputName.isEmpty())
            movieName.setError("Name is missing");
        else {
            ProgressDialog dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
            dialog.show();

            RetrofitClient.getInstance(URLConstant.BASE_URL).getApi().getMovie(inputName).enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    Movie movie = response.body();
                    if (movie != null && movie.getResponse().equalsIgnoreCase("true")) {
                        Glide.with(getApplicationContext()).load(movie.getPoster()).into(movieImg);
                        title.setText(movie.getTitle());
                        year.setText(movie.getYear());
                        duration.setText(movie.getRuntime());
                        actor.setText("Actors: " + movie.getActors());
                        genre.setText("Genre: " + movie.getGenre());
                        language.setText("Language: " + movie.getLanguage());
                        imdb.setText("IMDB: " + movie.getImdbRating());
                        plot.setText(movie.getPlot());
                        plot.setMovementMethod(new ScrollingMovementMethod());

                        placeHolder.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }else{
                        placeHolder.setText("Movie not found");
                        dialog.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    placeHolder.setText(t.getLocalizedMessage());
                    dialog.dismiss();
                }
            });
        }
    }
}