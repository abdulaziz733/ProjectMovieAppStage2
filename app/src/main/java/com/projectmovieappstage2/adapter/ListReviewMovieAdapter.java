package com.projectmovieappstage2.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projectmovieappstage2.R;
import com.projectmovieappstage2.interfaces.OnLoadMoreListener;
import com.projectmovieappstage2.model.Movie;
import com.projectmovieappstage2.model.Review;
import com.projectmovieappstage2.util.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abdul on 8/6/2017.
 */

public class ListReviewMovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean isLoading;
    private List<Review> reviewList;
    private Context context;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener onLoadMoreListener;

    public ListReviewMovieAdapter(RecyclerView recyclerView, List<Review> reviewList1, final Context context) {
        this.reviewList = reviewList1;
        this.context = context;


        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                totalItemCount = mLayoutManager.getItemCount();
//                lastVisibleItem = gridLayoutManager.findLastVisibleItemPositions();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_movie_item, parent, false);

            return new ListReviewMovieAdapter.DataViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);

            return new ListReviewMovieAdapter.LoadingViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListReviewMovieAdapter.DataViewHolder) {
            Review review = reviewList.get(position);
            DataViewHolder dataViewHolder = (DataViewHolder) holder;
            dataViewHolder.reviewAuthor.setText(review.getAuthor());
            dataViewHolder.reviewContent.setText(review.getContent());


        } else if (holder instanceof ListReviewMovieAdapter.LoadingViewHolder) {
            ListReviewMovieAdapter.LoadingViewHolder loadingViewHolder = (ListReviewMovieAdapter.LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return reviewList == null ? 0 : reviewList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return reviewList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView reviewAuthor, reviewContent;

        public DataViewHolder(View itemView) {
            super(itemView);

            reviewAuthor = (TextView) itemView.findViewById(R.id.review_author);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content);

        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    public interface ListReviewMovieAdapterListener {

        void onRowClicked(int position);

    }
}
