package com.projectmovieappstage2.api;


import com.projectmovieappstage2.model.api.ListPopularMovie;
import com.projectmovieappstage2.model.api.ListReviewMovie;
import com.projectmovieappstage2.model.api.ListTrailerMovie;
import com.projectmovieappstage2.util.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by abdul on 6/13/2017.
 */

public interface ApiInterface {

    @GET(Constant.POPULAR_MOVIE)
    Call<ListPopularMovie> getListPopularMovie(@Path("type_sort") String typeSort,
                                               @Query("language") String lang,
                                               @Query("page") int page);

    @GET(Constant.TRAILER_MOVIE)
    Call<ListTrailerMovie> getListMovieTrailers(@Path("movie_id") Integer movieId,
                                                @Query("language") String lang);

    @GET(Constant.REVIEWS_MOVIE)
    Call<ListReviewMovie> getListMovieReviews(@Path("movie_id") Integer movieId,
                                              @Query("language") String lang,
                                              @Query("page") int page);

}
