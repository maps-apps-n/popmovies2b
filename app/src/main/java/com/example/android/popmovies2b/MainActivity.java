package com.example.android.popmovies2b;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    private Toolbar toolbar;
    private List<Movies> movList;
    private RecyclerView rv;
    private MoviesAdapter adapter;
    boolean first = true;
    private static final int CURSOR_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        rv = (RecyclerView) findViewById(R.id.rv);
        movList = new ArrayList<>();
        rv.setHasFixedSize(true);

        adapter = new MoviesAdapter(movList, new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movies item) {
                Intent i = new Intent(MainActivity.this, MoviesInfo.class);
                i.putExtra("MOVIE", item);
                startActivity(i);
            }
        });

        rv.setAdapter(adapter);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            rv.setLayoutManager(new GridLayoutManager(this, 4));
        }
        new MainAsync().execute("popular");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Contract.Entry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Movies> movieList = new ArrayList<>();
        if (data != null && data.moveToFirst()) {
            do {
                String id = data.getString(Contract.Entry.MOV_ID);
                String title = data.getString(Contract.Entry.MOV_TITLE);
                String posterPath = data.getString(Contract.Entry.POS_PATH);
                String overview = data.getString(Contract.Entry.DESC);
                String rating = data.getString(Contract.Entry.VOTE_AVE);
                String releaseDate = data.getString(Contract.Entry.RELEASED);
                Movies movies = new Movies();
                movies.setMovieId(id);
                movies.setMovieTitle(title);
                movies.setMoviePoster(posterPath);
                movies.setMovieOverview(overview);
                movies.setMovieVote(rating);
                movies.setMovieRelease(releaseDate);
                movieList.add(movies);
            } while (data.moveToNext());
        }
        adapter.clear();
        adapter.add(movieList);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

    public class MainAsync extends AsyncTask<String, Void, Integer> {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            movList.clear();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String strUrl = "http://api.themoviedb.org/3/movie/" + params[0] + "?api_key=" + BuildConfig.API_KEY;
                URL url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                json = buffer.toString();
                convertJSON(json);
                return 1;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }

        private void convertJSON(String json) {
            final String objectResults = "results";
            final String id = "id";
            final String movTitle = "original_title";
            final String movPos = "poster_path";
            final String movDesc = "overview";
            final String movVote = "vote_average";
            final String movReleased = "release_date";
            try {
                JSONObject jsobject = new JSONObject(json);
                JSONArray jArray = jsobject.getJSONArray(objectResults);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject j = jArray.getJSONObject(i);
                    Movies movies = new Movies();
                    movies.setMovieId(j.getString(id));
                    movies.setMovieTitle(j.getString(movTitle));
                    movies.setMoviePoster(j.getString(movPos));
                    movies.setMovieOverview(j.getString(movDesc));
                    movies.setMovieVote(j.getString(movVote));
                    movies.setMovieRelease(j.getString(movReleased));
                    movList.add(movies);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 0)
                Toast.makeText(MainActivity.this, "Could not obtain data", Toast.LENGTH_LONG).show();
            else {
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("KeyForLayoutManagerState", rv.getLayoutManager().onSaveInstanceState());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable("KeyForLayoutManagerState");
            rv.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.top) {
            new MainAsync().execute("top_rated");
            return true;
        } else if (id == R.id.pop) {
            new MainAsync().execute("popular");
            return true;
        } else if (id == R.id.fav) {
            if (first == true) {
                getSupportLoaderManager().initLoader(CURSOR_ID, null, this);
                first = false;
            } else {
                getSupportLoaderManager().restartLoader(CURSOR_ID, null, this);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
