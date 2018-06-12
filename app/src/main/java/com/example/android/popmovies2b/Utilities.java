package com.example.android.popmovies2b;

import android.util.Log;

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

public class Utilities {

    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection connectivity = null;
        BufferedReader reader = null;
        try {
            connectivity = (HttpURLConnection) url.openConnection();
            connectivity.setRequestMethod("GET");
            connectivity.connect();
            InputStream inputStream = connectivity.getInputStream();
            if (connectivity.getResponseCode() == 200) {
                inputStream = connectivity.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                jsonResponse = stringBuffer.toString();

            } else { }
        } catch (IOException e) {
        } finally {
            if (connectivity != null) {
                connectivity.disconnect();
            }
        }
        return jsonResponse;
    }

    private static ArrayList<Reviews> extractReviewsFromJson(String url) {
        ArrayList<Reviews> revList = new ArrayList<>();
        final String MOVIE_ID = "id";
        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        try {
            JSONObject revObject = new JSONObject(url);
            String movieId = revObject.getString(MOVIE_ID);
            JSONArray revArray = revObject.getJSONArray(RESULTS);
            for (int i = 0; i < revArray.length(); i++) {
                JSONObject RevObject = revArray.getJSONObject(i);
                String author = RevObject.getString(AUTHOR);
                String content = RevObject.getString(CONTENT);
                Reviews reviews = new Reviews();
                reviews.setId(movieId);
                reviews.setAuthor(author);
                reviews.setContent(content);

                revList.add(reviews);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return revList;
    }

    public static ArrayList<Reviews> fetchReviewData(String requestUrl) {
        URL url = createURL(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Reviews> reviews = extractReviewsFromJson(jsonResponse);
        Log.v("reviews", reviews.toString());
        return reviews;
    }


    private static ArrayList<Trailers> extractTrailersFromJson(String url) {
        ArrayList<Trailers> tlist = new ArrayList<>();
        final String MOVIE_ID = "id";
        final String TRAILER_ID = "id";
        final String RESULTS = "results";
        final String KEY = "key";
        final String NAME = "name";
        try {
            JSONObject tObject = new JSONObject(url);
            String movieId = tObject.getString(MOVIE_ID);
            JSONArray tArray = tObject.getJSONArray(RESULTS);
            for (int i = 0; i < tArray.length(); i++) {
                JSONObject trailersObject = tArray.getJSONObject(i);
                String trailerId = trailersObject.getString(TRAILER_ID);
                String key = trailersObject.getString(KEY);
                String name = trailersObject.getString(NAME);
                Trailers trailers = new Trailers();
                trailers.setMovieId(movieId);
                trailers.setTrailerId(trailerId);
                trailers.setKey(key);
                trailers.setName(name);
                tlist.add(trailers);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tlist;
    }

    public static ArrayList<Trailers> fetchTrailersData(String requestUrl) {
        URL url = createURL(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Trailers> trailers = extractTrailersFromJson(jsonResponse);
        return trailers;
    }
}
