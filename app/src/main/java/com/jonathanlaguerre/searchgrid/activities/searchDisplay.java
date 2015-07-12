package com.jonathanlaguerre.searchgrid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.jonathanlaguerre.searchgrid.R;
import com.jonathanlaguerre.searchgrid.models.ImageResult;
import com.squareup.picasso.Picasso;

public class searchDisplay extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_display);
        getActionBar().hide();
        //Pull out url from intent
        ImageResult result = (ImageResult)getIntent().getSerializableExtra("result");

        //Find the image view
        ImageView ivImageResult= (ImageView)findViewById(R.id.ivImageResult);
        //load image url with picasso
        Picasso.with(this).load(result.fullUrl).into(ivImageResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_display, menu);
        return true;
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
