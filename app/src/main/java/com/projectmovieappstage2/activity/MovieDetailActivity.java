package com.projectmovieappstage2.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.projectmovieappstage2.R;
import com.projectmovieappstage2.adapter.ListReviewMovieAdapter;
import com.projectmovieappstage2.adapter.ListTrailerMovieAdapter;
import com.projectmovieappstage2.api.ApiInterface;
import com.projectmovieappstage2.data.MovieContract;
import com.projectmovieappstage2.interfaces.OnLoadMoreListener;
import com.projectmovieappstage2.model.Movie;
import com.projectmovieappstage2.model.Review;
import com.projectmovieappstage2.model.Trailer;
import com.projectmovieappstage2.model.api.ListReviewMovie;
import com.projectmovieappstage2.model.api.ListTrailerMovie;
import com.projectmovieappstage2.util.Constant;
import com.projectmovieappstage2.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private Intent intent;
    private Movie movie;
    private TextView txtMovieOriginalTitle, txtMovieReleaseDate, txtMovieRatting, txtMovieOverview, backDropTitle, popularity;
    private ImageView backDrop, poster;

    private ApiInterface apiInterface;
    private List<Review> reviewList;
    private List<Trailer> trailerList;
    private ListReviewMovieAdapter reviewMovieAdapter;
    private ListTrailerMovieAdapter trailerMovieAdapter;
    private RecyclerView rvListReview;
    private RecyclerView rvListTrailer;
    private String lang = "en-US";
    private int page = 2;
    private boolean firstloaded = false;

    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        apiInterface = Utils.getAPIService();

        txtMovieOriginalTitle = (TextView) findViewById(R.id.movie_title);
        txtMovieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        txtMovieRatting = (TextView) findViewById(R.id.movie_ratting);
        txtMovieOverview = (TextView) findViewById(R.id.movie_overview);
        poster = (ImageView) findViewById(R.id.movie_poster);
        backDrop = (ImageView) findViewById(R.id.backdrop);
        backDropTitle = (TextView) findViewById(R.id.app_bg_title);
        popularity = (TextView) findViewById(R.id.app_bg_popularity);
        rvListReview = (RecyclerView) findViewById(R.id.list_review_movie);
        rvListTrailer = (RecyclerView) findViewById(R.id.list_trailer_movie);
        rvListReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        reviewList = new ArrayList<Review>();
        reviewMovieAdapter = new ListReviewMovieAdapter(rvListReview, reviewList, MovieDetailActivity.this);
        rvListReview.setAdapter(reviewMovieAdapter);
        rvListTrailer.setLayoutManager(new LinearLayoutManager(this));
        trailerList = new ArrayList<Trailer>();
        trailerMovieAdapter = new ListTrailerMovieAdapter(trailerList, MovieDetailActivity.this);
        rvListTrailer.setAdapter(trailerMovieAdapter);

        rvListReview.setNestedScrollingEnabled(false);
        rvListTrailer.setNestedScrollingEnabled(false);

        reviewMovieAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                reviewList.add(null);
                reviewMovieAdapter.notifyItemInserted(reviewList.size() - 1);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataReview(1);
                    }
                }, 3000);
//                Toast.makeText(MainActivity.this, "Complete " + page, Toast.LENGTH_SHORT).show();
            }
        });


        intent = getIntent();
        if (intent.hasExtra("detail_movie")) {
            String resultData = intent.getStringExtra("detail_movie");
            Gson gson = new Gson();
            movie = gson.fromJson(resultData, Movie.class);

            txtMovieOriginalTitle.setText(movie.getOriginalTitle());
            backDropTitle.setText(movie.getOriginalTitle());
            popularity.setText("Popularity" + movie.getPopularity());
            txtMovieReleaseDate.setText(movie.getReleaseDate());
            txtMovieRatting.setText(movie.getVoteAverage().toString());
            txtMovieOverview.setText(movie.getOverview());

            Picasso.with(this)
                    .load(Constant.BASE_IMAGE_URL + movie.getPosterPath())
                    .placeholder(R.drawable.movie_placeholder)
                    .error(R.drawable.error_image)
                    .into(poster);

            Picasso.with(this)
                    .load(Constant.BASE_IMAGE_URL + movie.getBackdropPath())
                    .placeholder(R.drawable.movie_placeholder)
                    .error(R.drawable.error_image)
                    .into(backDrop);

        }

        fab = (FloatingActionButton) findViewById(R.id.fab);

        getDataReview(0);
        getDataTrailer();

        if (checkMovie()) {
            fab.setImageResource(R.drawable.ic_star_black_24dp);
        }

    }


    public void markAsFavorite(View v) {

        if (checkMovie()) {
            fab.setImageResource(R.drawable.ic_star_black_24dp);
            Toast.makeText(this, "Movie Already as Favorite ", Toast.LENGTH_SHORT).show();
        } else {
            addFavorite();
        }
    }

    private boolean checkMovie() {
        boolean check = false;
        String stringId = String.valueOf(movie.getId());
        Uri uri = MovieContract.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                int titleIndex = cursor.getColumnIndex(MovieContract.COLUMN_TITLE);
                check = true;
                fab.setImageResource(R.drawable.ic_star_black_24dp);
            } else {
                check = false;
                fab.setImageResource(R.drawable.ic_star_border_black_24dp);

            }

        } catch (Exception e) {
            check = false;
            Log.e(TAG, "Failed to asynchronously load data.");
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return check;
    }

    private void addFavorite() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.COLUMN_ID_MOVIE, movie.getId());
        contentValues.put(MovieContract.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieContract.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        contentValues.put(MovieContract.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieContract.COLUMN_VOTE_AVERAGE, String.valueOf(movie.getVoteAverage()));
        contentValues.put(MovieContract.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieContract.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        contentValues.put(MovieContract.COLUMN_POPULARITY, String.valueOf(movie.getPopularity()));

        Uri uri = getContentResolver().insert(MovieContract.CONTENT_URI, contentValues);
        if (uri != null) {
//            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Movie added to my Favorite", Toast.LENGTH_SHORT).show();
        }

        fab.setImageResource(R.drawable.ic_star_black_24dp);

    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(movie.getTitle());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    // typeRequest = 0 for firstload / reload and 1 for loadMore
    private void getDataReview(final int typeRequeest) {

        try {

            if (typeRequeest == 0) {
                reviewList.clear();
                page = 1;
            } else if (typeRequeest == 1) {
                page++;
//                Toast.makeText(this, "page " + page, Toast.LENGTH_SHORT).show();
            }

            apiInterface.getListMovieReviews(movie.getId(), lang, page).enqueue(new Callback<ListReviewMovie>() {
                @Override
                public void onResponse(Call<ListReviewMovie> call, Response<ListReviewMovie> response) {
                    if (response.isSuccessful()) {

                        if (firstloaded) {
                            if (!reviewList.isEmpty()) {
                                reviewList.remove(reviewList.size() - 1);
                                reviewMovieAdapter.notifyItemRemoved(reviewList.size());
                            }
                        }

                        List<Review> resultList = response.body().getResults();
                        if (!resultList.isEmpty()) {
                            for (Review review : resultList) {
                                reviewList.add(review);
                            }
                        }
                        firstloaded = true;
                        reviewMovieAdapter.notifyDataSetChanged();
                        reviewMovieAdapter.setLoaded();

                    }

                }

                @Override
                public void onFailure(Call<ListReviewMovie> call, Throwable t) {
                    Toast.makeText(MovieDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getDataTrailer() {
        try {

            trailerList.clear();

            apiInterface.getListMovieTrailers(movie.getId(), lang).enqueue(new Callback<ListTrailerMovie>() {
                @Override
                public void onResponse(Call<ListTrailerMovie> call, Response<ListTrailerMovie> response) {
                    if (response.isSuccessful()) {
                        List<Trailer> resultList = response.body().getResults();

                        for (Trailer trailer : resultList) {
                            trailerList.add(trailer);
                        }
                    }

                    trailerMovieAdapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(Call<ListTrailerMovie> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
