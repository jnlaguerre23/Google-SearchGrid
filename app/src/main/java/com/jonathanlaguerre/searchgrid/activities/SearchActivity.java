package com.jonathanlaguerre.searchgrid.activities;


import com.jonathanlaguerre.searchgrid.adapter.ImageResultsAdapter;
import com.jonathanlaguerre.searchgrid.helper.EndlessScrollListener;
import com.jonathanlaguerre.searchgrid.models.ImageResult;
import com.example.jonathanlaguerre.searchgrid.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import org.json.JSONObject;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class SearchActivity extends Activity {
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        //Create data source
        imageResults = new ArrayList<ImageResult>();
        //Attach data to adapter
        aImageResults= new ImageResultsAdapter(this, imageResults);
        //Attach adapter to gridview
        gvResults.setAdapter(aImageResults);
    }


    private void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView)findViewById(R.id.gvResults);
        gvResults.setOnScrollListener(new EndlessScrollListener() {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(false);
            }
        });
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Launch image display activity

                //Create intent
                Intent i = new Intent(SearchActivity.this, searchDisplay.class);
                //get image result
                ImageResult result = imageResults.get(position);
                //pass image result to intent
                i.putExtra("result", result);

                //Launch the new activity
                startActivity(i);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    public void onImageSearch(View v){
        String query = etQuery.getText().toString();
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+ query +"&rsz=8";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear();

                    aImageResults.addAll(ImageResult.fromJsonArray(imageResultsJson));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("INFO", imageResults.toString());
            }
        });

    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(final boolean isPage) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        String query = etQuery.getText().toString();
        String url = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+ query +"&rsz=8";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("DEBUG", response.toString());
                JSONArray imageResultsJson = null;
                try {
                    if (isPage) {
                        aImageResults.clear();
                    }
                    JSONObject cursor = response.getJSONObject("responseData").getJSONObject("cursor");
                    int currentPage = cursor.getInt("currentPageIndex");
                    JSONArray pages = cursor.getJSONArray("page");
                    if ((pages.length() - 1) > currentPage) {
                        JSONObject page = pages.getJSONObject(currentPage + 1);
                       // searchOptions.start = page.getString("start");
                    } else if (!isPage) {
                        // stop searching once we have reached the end
                        Toast.makeText(getApplicationContext(), "No more results for this search!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                    imageResults.clear();

                    aImageResults.addAll(ImageResult.fromJsonArray(imageResultsJson));
                    if (aImageResults.getCount() < 12) {
                        customLoadMoreDataFromApi(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("INFO", imageResults.toString());
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
