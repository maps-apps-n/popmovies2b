package com.example.android.popmovies2b;

import android.os.AsyncTask;

import java.util.ArrayList;



public class Callback extends AsyncTask<Void, Void, ArrayList<Reviews>> {

    ReviewsCb cback;

    public Callback(ReviewsCb cback) {
        this.cback = cback;
    }

    public interface ReviewsCb {
        void onReviewsReceived(ArrayList<Reviews> reviews);
    }

    @Override
    protected ArrayList<Reviews> doInBackground(Void... voids) {
        String reviewsUrl = "https://api.themoviedb.org/3/movie/" +
                MoviesInfo.movId + "/reviews" + "?api_key=" + BuildConfig.API_KEY;
        ArrayList<Reviews> revResults = Utilities.fetchReviewData(reviewsUrl);
        return revResults;
    }

    @Override
    protected void onPostExecute(ArrayList<Reviews> revList) {
        super.onPostExecute(revList);
        MoviesInfo.adapterRev.setRev(revList);
        cback.onReviewsReceived(revList);
    }
}
