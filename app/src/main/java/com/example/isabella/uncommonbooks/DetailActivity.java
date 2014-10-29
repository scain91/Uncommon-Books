package com.example.isabella.uncommonbooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


public class DetailActivity extends Activity {

    final String API_KEY = "1A:27:95:F1:1E:58:C2:AC:A3:93:56:1A:45:A2:01:DC:5E:60:BB:AD";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);


        //If coming from results list
        if(getIntent() != null && getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            TextView txtTitle = (TextView) findViewById(R.id.title_book);
            TextView txtAuthor = (TextView) findViewById(R.id.author_book);
            //TextView txtDescription = (TextView) findViewById(R.id.description_book);
            txtTitle.setText(b.getString("title"));
            txtAuthor.setText(b.getString("author"));
            //txtDescription.setText(b.getString("description"));
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.view_lists){
            Intent intent = new Intent(this, ListOfListsActivity.class);
            startActivity(intent);
            System.out.println("Got past startActivity");
        }
        return super.onOptionsItemSelected(item);
    }
}

