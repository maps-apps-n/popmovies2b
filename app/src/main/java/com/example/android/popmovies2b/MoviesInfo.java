package com.example.android.popmovies2b;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesInfo extends AppCompatActivity
        implements Callback.ReviewsCb, LoaderManager.LoaderCallbacks<ArrayList<Trailers>> {

    @BindView(R.id.poster_image)
    ImageView poster;
    @BindView(R.id.text_date)
    TextView releaseDate;
    @BindView(R.id.text_vote_ave)
    TextView voteAvg;
    @BindView(R.id.text_title)
    TextView title;
    @BindView(R.id.text_synopsis)
    TextView overview;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private boolean isBookmarked = false;

    private Movies movies;
    public ArrayList<Reviews> reviews = new ArrayList<>();
    public static ReviewsAdapter adapterRev;
    public static RecyclerView rvRev;

    private static final String REVIEWS_LIST = "reviewList";
    private static final int TRAILERS_LOADER_ID = 0;

    private ArrayList<Trailers> trailers = new ArrayList<>();
    public static TrailersAdapter adapterT;
    private RecyclerView rvT;

    public static String movId;
    String movie_id, thumbnail, movieName, synopsis, rating, release;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", title.getText().toString());
        outState.putString("rating", voteAvg.getText().toString());
        outState.putString("date", releaseDate.getText().toString());
        outState.putString("overview", overview.getText().toString());
        outState.putString("image", poster.toString());
        outState.putParcelableArrayList(REVIEWS_LIST, (ArrayList<? extends Parcelable>) reviews);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_info);
        ButterKnife.bind(this);
        Toolbar bar = (Toolbar) findViewById(R.id.tb);
        movies = (Movies) getIntent().getParcelableExtra("MOVIE");
        if (movies != null) {

            movie_id = movies.getMovieId();
            thumbnail = movies.getMoviePoster();
            movieName = movies.getMovieTitle();
            synopsis = movies.getMovieOverview();
            rating = movies.getDetailedVoteAverage();
            release = movies.getMovieRelease();

            Picasso.with(getApplicationContext()).
                    load("https://image.tmdb.org/t/p/w500" + thumbnail).into(poster);
            releaseDate.setText(release);
            voteAvg.setText(rating);
            title.setText(movieName);
            overview.setText(synopsis);
            fab.setSelected(movies.isFavorite());
        }

        movId = movies.getMovieId();
        getSupportLoaderManager().initLoader(TRAILERS_LOADER_ID, null, this);
        rvT = (RecyclerView) findViewById(R.id.tList);
        rvT.setLayoutManager(new LinearLayoutManager(this));
        rvT.setHasFixedSize(true);
        adapterT = new TrailersAdapter(this, trailers);
        rvT.setAdapter(adapterT);

        if (savedInstanceState == null) {
            rvRev = (RecyclerView) findViewById(R.id.rvreviews);
            rvRev.setLayoutManager(new LinearLayoutManager(this));
            adapterRev = new ReviewsAdapter(reviews);
            rvRev.setAdapter(adapterRev);
            new Callback(this).execute();
        } else {
            reviews = savedInstanceState.getParcelableArrayList(REVIEWS_LIST);
            rvRev = (RecyclerView) findViewById(R.id.rvreviews);
            rvRev.setLayoutManager(new LinearLayoutManager(this));
            adapterRev = new ReviewsAdapter(reviews);
            rvRev.setAdapter(adapterRev);
        }

        movies.setFavorite(checkBookmarked(movies.getMovieId()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movies.isFavorite()) {
                    getContentResolver().delete(Contract.Entry.CONTENT_URI.buildUpon()
                            .appendPath(String.valueOf(movies.getMovieId())).build(), null, null);
                    fab.setImageResource(R.drawable.baseline_bookmark_border_white_18dp);
                    Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_LONG).show();
                    movies.setFavorite(false);

                } else {
                    ContentValues values = DataHelper.getMovieContentValues(movies);
                    getContentResolver().insert(Contract.Entry.CONTENT_URI, values);
                    fab.setImageResource(R.drawable.baseline_bookmark_white_18dp);
                    Toast.makeText(getApplicationContext(), "Added To favorites", Toast.LENGTH_LONG).show();
                    movies.setFavorite(true);
                }
                fab.setSelected(movies.isFavorite());
            }
        });
    }

    private boolean checkBookmarked(String id) {
        Cursor cursor = getContentResolver().query(Contract.Entry.CONTENT_URI,
                null,
                Contract.Entry.COLUMN_MOV_ID + "='" + id + "'",
                null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                fab.setImageResource(R.drawable.baseline_bookmark_white_18dp);
                return true;
            }
            cursor.close();
        }
        fab.setImageResource(R.drawable.baseline_bookmark_border_white_18dp);
        return false;
    }

    @Override
    public void onReviewsReceived(ArrayList<Reviews> reviews) {
        this.reviews = reviews;
    }

    @Override
    public Loader<ArrayList<Trailers>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Trailers>>(this) {

            ArrayList<Trailers> trailers = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (trailers != null) {
                    deliverResult(trailers);
                } else {
                    forceLoad();
                }
            }

            public void deliverResult(ArrayList<Trailers> data) {
                trailers = data;
                super.deliverResult(data);
            }

            @Override
            public ArrayList<Trailers> loadInBackground() {
                String trailersUrl = "https://api.themoviedb.org/3/movie/" +
                        movId + "/videos" + "?api_key=" + BuildConfig.API_KEY;
                ArrayList<Trailers> results = Utilities.fetchTrailersData(trailersUrl);
                return results;
            }


        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Trailers>> loader, ArrayList<Trailers> data) {
        adapterT.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Trailers>> loader) {
    }
}
