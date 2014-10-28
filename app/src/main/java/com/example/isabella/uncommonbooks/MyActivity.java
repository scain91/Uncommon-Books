package com.example.isabella.uncommonbooks;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.api.services.books.Books;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;




public class MyActivity extends Activity {

    final String API_KEY = "1A:27:95:F1:1E:58:C2:AC:A3:93:56:1A:45:A2:01:DC:5E:60:BB:AD";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        System.out.println("Created");
        apiAccess();


    }

    protected void apiAccess(){
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        try {
            final Books books = new Books.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null)
                    .setApplicationName("")
                    .setGoogleClientRequestInitializer(new BooksRequestInitializer(API_KEY))
                    .build();
            System.out.println("after books");
        }
        catch(Exception e){
            System.out.println("Caught\n");
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
        return super.onOptionsItemSelected(item);
    }
}
