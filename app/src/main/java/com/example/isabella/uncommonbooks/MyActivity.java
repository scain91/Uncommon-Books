package com.example.isabella.uncommonbooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import com.google.api.services.books.Books.Volumes.List;

//import com.google.api.client.http.HttpResponse;
import com.google.api.services.books.Books;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.Books.Volumes.List;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

//import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


public class MyActivity extends Activity {

    final private String ANDROID_KEY = "AIzaSyBEqI9HdDpSoxszElDQJ16DgBmc8aRzYu8";

    private boolean selected_genres[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        selected_genres = new boolean[6];
        //Grab genres from Google
        System.out.println("Created");
        new ApiAccess().execute();


    }

    private class ApiAccess extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void...arg0) {

            try
            {
                Log.d("blah", "Before");
                final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
                final Books books = new Books.Builder(AndroidHttp.newCompatibleTransport(), jsonFactory, null)
                        .setApplicationName("API Project")
                        .setBooksRequestInitializer(new BooksRequestInitializer(ANDROID_KEY))
                        .build();
                Books.Volumes.List volumesList = books.volumes().list("Blue");
                Log.d("blah", "before executelk");
                Volumes volumes = volumesList.execute();
                Log.d("blah", volumes.toPrettyString());
                Log.d("blah", "Items: "+volumes.getTotalItems().toString());
                for(Volume volume: volumes.getItems()){
                    Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
                    Log.d("blah", volumeInfo.getTitle());
                    Log.d("blah", volumeInfo.getAuthors().toString());
                }
                Log.d("blah", "After");

            }

            catch(Exception e)

            {
                Log.d("blah", Log.getStackTraceString(e));
            }
            return null;
        }

        protected void onProgressUpdate(Void...arg0){}
        protected void onPostExecute(Void...arg0){}

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

    public void random_button_listener(View v) {
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
        System.out.println("In button listener");
    }

    public void search_button_listener(View v) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
        System.out.println("In button listener");
    }

    public void genre_button_listener(View v) {
        AlertDialog.Builder genrePopUp = new AlertDialog.Builder(this);
        genrePopUp.setTitle(R.string.choose_genre);
        genrePopUp.setMultiChoiceItems(R.array.genre_list, selected_genres, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int whichButton,
                                boolean isChecked) {

                /* User clicked on a check box do some stuff */
                System.out.println("Clicked button #" + whichButton);
            }
        });
        genrePopUp.show();
    }


}
