package com.projectmovieappstage2.data;

import android.net.Uri;

/**
 * Created by abdul on 8/8/2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.projectmovieappstage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "movies";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

    public static final String TABLE_NAME = "movies";
    public static final String _ID = "_id";
    public static final String COLUMN_ID_MOVIE = "id_movie";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
    public static final String COLUMN_POPULARITY = "popularity";
    public static final String _COUNT = "_count";
}
