package com.projectmovieappstage2.util;


import android.net.Uri;

import com.projectmovieappstage2.BuildConfig;

/**
 * Created by abdul on 6/13/2017.
 */

public class Constant {

    public static final String BASE_URL = "https://api.themoviedb.org";
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String POPULAR_MOVIE = BASE_URL + "/3/movie/{type_sort}?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN ;
    public static final String TRAILER_MOVIE = BASE_URL + "/3/movie/{movie_id}/videos?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN ;
    public static final String REVIEWS_MOVIE = BASE_URL + "/3/movie/{movie_id}/reviews?api_key=" + BuildConfig.THE_MOVIE_DB_API_TOKEN ;




}
