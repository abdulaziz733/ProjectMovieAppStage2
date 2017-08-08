package com.projectmovieappstage2.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.projectmovieappstage2.R;
import com.projectmovieappstage2.adapter.ListPopularModieAdapter;
import com.projectmovieappstage2.api.ApiInterface;
import com.projectmovieappstage2.interfaces.OnLoadMoreListener;
import com.projectmovieappstage2.model.Movie;
import com.projectmovieappstage2.model.api.ListPopularMovie;
import com.projectmovieappstage2.util.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ListPopularModieAdapter.ListPopularModieAdapterListener {

    private ApiInterface apiInterface;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private ListPopularModieAdapter adapter;
    private List<Movie> movieList;
    private String lang = "en-US";
    private int page = 2;
    private boolean firstloaded = false;
    private String typeSort = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiInterface = Utils.getAPIService();

        recyclerView = (RecyclerView) findViewById(R.id.list_Popular_movie);
//        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_movie_refresh);
        refreshLayout.setOnRefreshListener(this);
        movieList = new ArrayList<Movie>();
        adapter = new ListPopularModieAdapter(recyclerView, movieList, MainActivity.this, MainActivity.this);
        recyclerView.setAdapter(adapter);

        typeSort = "popular";

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                movieList.add(null);
                adapter.notifyItemInserted(movieList.size() - 1);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDataMovie(1, typeSort);
                    }
                }, 5000);
//                Toast.makeText(MainActivity.this, "Complete " + page, Toast.LENGTH_SHORT).show();
            }
        });


        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getDataMovie(0, typeSort);
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int orientation = newConfig.orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, 1));
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_movie_menu, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sort_popular) {
            typeSort = "popular";
            getDataMovie(0, typeSort);
        } else if (id == R.id.sort_top_rated) {
            typeSort = "top_rated";
            getDataMovie(0, typeSort);
        } else if (id == R.id.my_favorite){
            Intent intent = new Intent(MainActivity.this, MyFavoriteActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // typeRequest = 0 for firstload / reload and 1 for loadMore
    private void getDataMovie(final int typeRequeest, String typeSort) {

        try {

            if (typeRequeest == 0) {
                movieList.clear();
                page = 1;
            } else if (typeRequeest == 1) {
                page++;
//                Toast.makeText(this, "page " + page, Toast.LENGTH_SHORT).show();
            }

            apiInterface.getListPopularMovie(typeSort, lang, page).enqueue(new Callback<ListPopularMovie>() {
                @Override
                public void onResponse(Call<ListPopularMovie> call, Response<ListPopularMovie> response) {
                    if (response.isSuccessful()) {
                        if (firstloaded) {
                            if (!movieList.isEmpty()) {
                                movieList.remove(movieList.size() - 1);
                                adapter.notifyItemRemoved(movieList.size());
                            }
                        }

                        List<Movie> resultList = response.body().getResults();
                        if (!resultList.isEmpty()) {
                            for (Movie movie : resultList) {
                                movieList.add(movie);
                            }
                        }
                        firstloaded = true;
                        adapter.notifyDataSetChanged();
                        adapter.setLoaded();
                        refreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<ListPopularMovie> call, Throwable t) {
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            refreshLayout.setRefreshing(false);
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRefresh() {
        getDataMovie(0, typeSort);
    }

    @Override
    public void onRowClicked(int position) {
        Movie movie = movieList.get(position);
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        Gson gson = new Gson();
        String dataIntent = gson.toJson(movie);
        intent.putExtra("detail_movie", dataIntent);
        startActivity(intent);
    }
}
