package com.example.ugo.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class FootballLoader extends AsyncTaskLoader<List<Football>> {

    /* Tag for log messages */
    private static final String LOG_TAG = FootballLoader.class.getName();

    /* Query URL */
    private String mUrl;

    /*
     * Constructs a new {@link FootballLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public FootballLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /*
     * This is on a background thread.
     */
    @Override
    public List<Football> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of football news.
        List<Football> footballNewsItems = QueryUtils.fetchFootballData(mUrl);
        return footballNewsItems;
    }
}
