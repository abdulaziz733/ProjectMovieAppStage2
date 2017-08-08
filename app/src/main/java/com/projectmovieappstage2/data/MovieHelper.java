package com.projectmovieappstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.projectmovieappstage2.util.Constant;

/**
 * Created by abdul on 8/8/2017.
 */

public class MovieHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieDb.db";

    private static final int VERSION = 1;

    MovieHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " + MovieContract.TABLE_NAME + " (" +
                MovieContract._ID + " INTEGER PRIMARY KEY, " +
                MovieContract.COLUMN_ID_MOVIE + " INTEGER NOT NULL, " +
                MovieContract.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieContract.COLUMN_POPULARITY + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
