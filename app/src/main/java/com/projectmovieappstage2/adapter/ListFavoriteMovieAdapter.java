package com.projectmovieappstage2.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.IntegerRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.projectmovieappstage2.R;
import com.projectmovieappstage2.activity.MainActivity;
import com.projectmovieappstage2.activity.MovieDetailActivity;
import com.projectmovieappstage2.data.MovieContract;
import com.projectmovieappstage2.model.Movie;
import com.projectmovieappstage2.util.Constant;
import com.squareup.picasso.Picasso;

/**
 * Created by abdul on 8/8/2017.
 */

public class ListFavoriteMovieAdapter extends RecyclerView.Adapter<ListFavoriteMovieAdapter.DataViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public ListFavoriteMovieAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.favorite_movie_item, parent, false);

        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {

        int idIndex = mCursor.getColumnIndex(MovieContract._ID);
        int idMovieIndex = mCursor.getColumnIndex(MovieContract.COLUMN_ID_MOVIE);
        int titleIndex = mCursor.getColumnIndex(MovieContract.COLUMN_TITLE);
        int originalTitleIndex = mCursor.getColumnIndex(MovieContract.COLUMN_ORIGINAL_TITLE);
        int releaseDateIndex = mCursor.getColumnIndex(MovieContract.COLUMN_RELEASE_DATE);
        int voteAverageIndex = mCursor.getColumnIndex(MovieContract.COLUMN_VOTE_AVERAGE);
        int overviewIndex = mCursor.getColumnIndex(MovieContract.COLUMN_OVERVIEW);
        int posterPathIndex = mCursor.getColumnIndex(MovieContract.COLUMN_POSTER_PATH);
        int backdropPathIndex = mCursor.getColumnIndex(MovieContract.COLUMN_BACKDROP_PATH);
        int popularityIndex = mCursor.getColumnIndex(MovieContract.COLUMN_POPULARITY);

        mCursor.moveToPosition(position);

        holder.itemView.setTag(mCursor.getInt(idMovieIndex));

        final Movie sendMovie = new Movie();
        sendMovie.setId(mCursor.getInt(idMovieIndex));
        sendMovie.setTitle(mCursor.getString(titleIndex));
        sendMovie.setOriginalTitle(mCursor.getString(originalTitleIndex));
        sendMovie.setReleaseDate(mCursor.getString(releaseDateIndex));
        sendMovie.setVoteAverage(Double.parseDouble(mCursor.getString(voteAverageIndex)));
        sendMovie.setOverview(mCursor.getString(overviewIndex));
        sendMovie.setPosterPath(mCursor.getString(posterPathIndex));
        sendMovie.setBackdropPath(mCursor.getString(backdropPathIndex));
        sendMovie.setPopularity(Double.parseDouble(mCursor.getString(popularityIndex)));

        holder.movieTitle.setText(mCursor.getString(titleIndex));
        holder.moviePopularity.setText("Popularity: " + mCursor.getString(popularityIndex));
        Picasso.with(mContext)
                .load(Constant.BASE_IMAGE_URL + mCursor.getString(posterPathIndex))
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.error_image)
                .into(holder.moviePoster);

        holder.viewMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MovieDetailActivity.class);
                Gson gson = new Gson();
                String dataIntent = gson.toJson(sendMovie);
                intent.putExtra("detail_movie", dataIntent);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        if (mCursor == c) {
            return null;
        }

        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    class DataViewHolder extends RecyclerView.ViewHolder {

        private ImageView moviePoster;
        private TextView movieTitle, moviePopularity;
        private LinearLayout viewMovie;

        public DataViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.img_movie);
            movieTitle = (TextView) itemView.findViewById(R.id.title_movie);
            moviePopularity = (TextView) itemView.findViewById(R.id.popularity_movie);
            viewMovie = (LinearLayout) itemView.findViewById(R.id.view_item_movie);

        }
    }

}
