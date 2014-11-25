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
import android.widget.EditText;

import java.util.ArrayList;


public class MyActivity extends Activity {

    private boolean selected_genres[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        //Grab genres from Google and set the size of selected_genres to the number we find
        selected_genres = new boolean[6];
        System.out.println("Created");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        EditText et = (EditText) findViewById(R.id.keywords);
        String s = et.getText().toString();
        Log.d("blah", "searching for " + s);
        intent.putExtra("search", s);
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
