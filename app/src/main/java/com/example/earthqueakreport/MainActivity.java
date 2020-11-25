package com.example.earthqueakreport;

//import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
//import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    //ArrayList<report> reports = new ArrayList<>();
     ArrayList<report> reports = new ArrayList<>();
     private ListView lv;
     private TextView empty;
     private ProgressBar pbar;
     private MenuItem action_settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         lv = findViewById(R.id.lv);
         action_settings = findViewById(R.id.action_settings);
//          reports = quakeReport.getReport();
//        EarthquakeAsyncTask asyncTask = new EarthquakeAsyncTask();
//        asyncTask.execute(USGS_REQUEST_URL);
//
        empty = findViewById(R.id.empty);
        pbar = findViewById(R.id.pbar);
//        LoaderManager loaderManager = getLoaderManager();
        getSupportLoaderManager().initLoader(1, null, this);
        System.out.println("int inti methode");
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
       // loaderManager.initLoader(1, null, this);
       lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = reports.get(i).getUrl();
               Intent intent = new Intent(Intent.ACTION_VIEW);
               intent.setData(Uri.parse(url));
               startActivity(intent);
           }
       });


        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
    private boolean isConnected() {
        // Check for connectivity status
        ConnectivityManager cm = (ConnectivityManager) this.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id,  Bundle args) {
        System.out.println("in on create loader");
        return new EarthAsyncLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        System.out.println("in on finished loader");
        reports.addAll(getReport(data));
        pbar.setVisibility(View.GONE);
        reportAdapter adapter = new reportAdapter(reports,MainActivity.this);
        lv.setAdapter(adapter);
        if(!isInternetAvailable()){
            empty.setText("NO INTERNET ACCESS");
            empty.setVisibility(View.VISIBLE);
        }
       else if(reports.size()==0){
            empty.setVisibility(View.VISIBLE);
        }
        lv.setEmptyView(empty);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        System.out.println("in on reset loader");
       reports.clear();
    }

//    private class EarthquakeAsyncTask extends AsyncTask<String, Void, String> {
//
//
//
//        /**
//         * This method runs on a background thread and performs the network request.
//         * We should not update the UI from a background thread, so we return a list of
//         * {@link report}s as the result.
//         */
//        @Override
//        protected String doInBackground(String... urls) {
//            // Don't perform the request if there are no URLs, or the first URL is null.
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//           String result = quakeReport.fetchEarthquakeData(urls[0]);
//            return result;
//        }
//
//        /**
//         * This method runs on the main UI thread after the background work has been
//         * completed. This method receives as input, the return value from the doInBackground()
//         * method. First we clear out the adapter, to get rid of earthquake data from a previous
//         * query to USGS. Then we update the adapter with the new list of earthquakes,
//         * which will trigger the ListView to re-populate its list items.
//         */
//        @Override
//        protected void onPostExecute(String data) {
//            reports.addAll(getReport(data));
//            reportAdapter adapter = new reportAdapter(reports,MainActivity.this);
//            lv.setAdapter(adapter);
//        }
//    }
    public static class EarthAsyncLoader extends AsyncTaskLoader<String> {
  public  String mUrl;
        public EarthAsyncLoader(@NonNull Context context,String url) {

            super(context);
            mUrl  = url;
        }
        @Override
        protected void onStartLoading() {
            System.out.println("in on start loading loader");
            forceLoad();
        }

        @Nullable
        @Override
        public String loadInBackground() {
            System.out.println("in on load in background loader");
            // Don't perform the request if there are no URLs, or the first URL is null.
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
            String result = quakeReport.fetchEarthquakeData(USGS_REQUEST_URL);
            return result;
        }

    }
    public static ArrayList<report> getReport(String SAMPLE_JSON_RESPONSE) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<report> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject root = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray array = root.getJSONArray("features");
            for (int i=0;i<array.length();i++){
                JSONObject curr =  array.getJSONObject(i);
                JSONObject prop = curr.getJSONObject("properties");
                String url = prop.getString("url");
                double mag = prop.getDouble("mag");
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                decimalFormat.format(mag);
                String place = prop.getString("place");
                String time = prop.getString("time");
                Long t = Long.parseLong(time);
//                Date date = new Date(t);
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM DD, yyyy");
//                String dateToD = simpleDateFormat.format(date);
                SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");
                 String dateToD = formatter.format(t);
                report report = new report(mag,place,dateToD,url);
                earthquakes.add(report);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}
