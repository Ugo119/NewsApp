package com.example.ugo.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Football>> {

    /*
     * Constant value for the football loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int FOOTBALL_LOADER_ID = 1;

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    /* TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /* Adapter for the list of football news */
    private FootballAdapter mAdapter;

    /* URL for football data from the Guardian dataset */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?q=english-premier-league&from-date=2018-10-03&to-date=2018-10-04&api-key=b7042a7f-9de6-42d2-9d53-76c559a9cfe0";
    // "https://content.guardianapis.com/search?q=arsenal&from-date=2018-10-01&to-date=2018-10-03&api-key=b7042a7f-9de6-42d2-9d53-76c559a9cfe0";

    //This method checks if device is connected to a network
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    //This method checks if device is connected to the internet
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("https://content.guardianapis.com/search?q=english-premier-league&from-date=2018-10-03&to-date=2018-10-04&api-key=b7042a7f-9de6-42d2-9d53-76c559a9cfe0");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Find a reference to the {@Link ListView} in the Layout
        ListView listView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of football news as input
        mAdapter = new FootballAdapter(this, new ArrayList<Football>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Find the current football news item that was clicked on
                Football currentFootball = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri footballUri = Uri.parse(currentFootball.getUrl());

                // Create a new intent to view the football news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, footballUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        Log.i(LOG_TAG, "TEST: calling initLoader() ...");
        loaderManager.initLoader(FOOTBALL_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Football>> onCreateLoader(int i, Bundle bundle) {

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=json`
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("orderby", "time");

        // Return the completed uri `https://content.guardianapis.com/search?q=arsenal&from-date=2018-09-30&date=2018-10-01&api-key=b7042a7f-9de6-42d2-9d53-76c559a9cfe0
        return new FootballLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Football>> loader, List<Football> footballs) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No no football news found."
        mEmptyStateTextView.setText(R.string.no_footballnews);


        // Clear the adapter of previous football data
        mAdapter.clear();

        // If there is a valid list of {@link Football}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (footballs != null && !footballs.isEmpty()) {
            mAdapter.addAll(footballs);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Football>> loader) {
        // Loader reset, so we can clear out our existing data.
        Log.i(LOG_TAG, "TEST: onLoaderReset() called ...");
        mAdapter.clear();
    }


}
