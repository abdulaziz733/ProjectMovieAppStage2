package com.projectmovieappstage2.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projectmovieappstage2.R;
import com.projectmovieappstage2.model.Trailer;

import java.util.List;

/**
 * Created by abdul on 8/8/2017.
 */

public class ListTrailerMovieAdapter extends RecyclerView.Adapter<ListTrailerMovieAdapter.DataViewHolder> {

    private List<Trailer> trailerList;
    private Context context;

    public ListTrailerMovieAdapter(List<Trailer> trailerList, Context context) {
        this.trailerList = trailerList;
        this.context = context;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_movie_item, parent, false);
        return new ListTrailerMovieAdapter.DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        final Trailer trailer = trailerList.get(position);
        holder.trailerName.setText(trailer.getName());
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchYoutubeVideo(trailer.getKey());
            }
        });

    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }


    public class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView trailerName;
        private LinearLayout play;

        public DataViewHolder(View itemView) {
            super(itemView);

            trailerName = (TextView) itemView.findViewById(R.id.trailer_movie_name);
            play = (LinearLayout) itemView.findViewById(R.id.trailer_action_play);

        }
    }

}
