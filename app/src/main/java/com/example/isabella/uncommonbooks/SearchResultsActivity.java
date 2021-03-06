package com.example.isabella.uncommonbooks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.books.Books;
import com.google.api.services.books.BooksRequestInitializer;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volumes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SearchResultsActivity extends Activity {

    protected ArrayList<Book> book_list;
    protected Dialog mSplashDialog;
    protected BookViewAdapter adapter;
    protected boolean freeEBooks;
    public final static int MAX_RESULTS = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        freeEBooks = false;
        //get input string
        String s = "";
        if(getIntent() != null && getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            s = b.getString("search");
            freeEBooks = b.getBoolean("ebooks");
        }
        Log.d("blah", "search received: " + s);
        Log.d("blah", "ebooks only? " + freeEBooks);
        showSplashScreen();

        //API calls here//
        setContentView(R.layout.activity_search_results);
        ApiAccess api = new ApiAccess();
        api.execute(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_results, menu);
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
        /*if(id == R.id.view_home) {
            Intent intent = new Intent(this, MyActivity.class);
            startActivity(intent);
            Log.d("blah", "starting MyActivity");
        }*/
        else if(id == R.id.view_lists){
            Intent intent = new Intent(this, ListOfListsActivity.class);
            startActivity(intent);
            System.out.println("Got past startActivity");
        }
        return super.onOptionsItemSelected(item);
    }


    private class ApiAccess extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String...arg0) {
            try
            {
                Log.d("blah", "Just entered doInBackground of ApiAccess");
                final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
                final Books books = new Books.Builder(AndroidHttp.newCompatibleTransport(), jsonFactory, null)
                        .setApplicationName("API Project")
                        .setBooksRequestInitializer(new BooksRequestInitializer(getString(R.string.ANDROID_KEY)))
                        .build();


                /* TODO: implement heuristic for uncommon books
                 */
                Books.Volumes.List volumesList = books.volumes().list(arg0[0]);
                volumesList.setMaxResults(new Long(40));
                /*This is where we can change the results of the search
                  Example:
                    volumesList.setMaxResults((long) 1);
                    changes the search to only retrieve 1 book

                  All methods can be found at
                    https://developers.google.com/resources/api-libraries/documentation/books/v1/java/latest/
                */
                if(freeEBooks)
                    volumesList.setFilter("ebooks");

//                Log.d("blah", "before execute");
//
//                Log.d("blah", "after execute about to start adding to list");
//                Log.d("blah", volumes.toPrettyString());
                //Log.d("blah", "Items: "+volumes.getTotalItems().toString());
                long startIndex = 0;
                book_list = new ArrayList<Book>();
                while(book_list.size() < 5){
                    volumesList.setStartIndex(startIndex);
                    Volumes volumes = volumesList.execute();
                    startIndex += MAX_RESULTS;
                    if (volumes == null || volumes.getTotalItems() == 0) {
                        Log.d("blah", "no more");
                        break;
                    }
                    else{
                        for (Volume volume : volumes.getItems()) {
                            Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
                            if (volumeInfo != null) {
                                Log.d("blah", volumeInfo.getTitle());
                                // Log.d("blah", volumeInfo.getAuthors().toString());
                                String title = volumeInfo.getTitle() != null ?
                                        volumeInfo.getTitle() : "title missing";
                                String author = volumeInfo.getAuthors() != null ?
                                        volumeInfo.getAuthors().toString() : "authors missing";
                                //removing brackets
                                author = author.substring(1, author.length() - 1);
                                String desc = volumeInfo.getDescription() != null ?
                                        volumeInfo.getDescription() : "description missing";
                                double avg = volumeInfo.getAverageRating() != null ?
                                        volumeInfo.getAverageRating() : -1;
                                int numRatings = volumeInfo.getRatingsCount() != null ?
                                        volumeInfo.getRatingsCount() : -1;

                                Bitmap img_bmp = null;  //make this a default image in future
                                Bitmap thumb_bmp = null; //thumbnail bitmap
                                try {
                                    //setting up large image
                                    if (volumeInfo.getImageLinks() != null) {
                                        String imageString = volumeInfo.getImageLinks().getThumbnail();
                                        URL imgUrl = new URL(imageString);
                                        Log.d("blah", "img = " + imageString);
                                        img_bmp = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
                                    }
                                    //setting up thumbnail image
                                /*String thumbString = volumeInfo.getImageLinks().getSmallThumbnail();
                                URL thumbUrl = new URL(thumbString);
                                Log.d("blah", "thumb = " + thumbString);
                                tbmp = BitmapFactory.decodeStream(thumbUrl.openConnection().getInputStream());*/
                                } catch (MalformedURLException e) {
                                    Log.d("Error: ", "bad URL");
                                } catch (IOException e) {
                                    Log.d("Error: ", "IOException using bitmap");
                                }
                                if (numRatings > MyActivity.MIN_NUM_RATINGS && numRatings < MyActivity.MAX_NUM_RATINGS && avg > MyActivity.MIN_AVG_RATING) {
                                    Book b = new Book(title, author, img_bmp, thumb_bmp, desc, avg, numRatings);
                                    book_list.add(b);
                                }
                            }
                        }
                    }
                }
                Log.d("blah", "all books added to list");
            }
            catch(Exception e){
                Log.d("blah", Log.getStackTraceString(e));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void...arg0){
            Log.d("blah", "entered onProgressUpdate");
        }

        @Override
        protected void onPostExecute(Void a){
            Log.d("blah", "entered onPostExecute");
            removeSplashScreen();
            ListView listview = (ListView) findViewById(R.id.results_listview);
            adapter = new BookViewAdapter(getApplicationContext(), R.layout.search_result_item, book_list);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id){
                    Book book = (Book) parent.getItemAtPosition(position);
                    Log.d("blah", "book being sent to DetailActivity = " + book.getTitle() + " by " + book.getAuthor());
                    //Show details of the book
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    Bundle b = new Bundle();
                    b.putString("title", book.getTitle());
                    b.putString("author", book.getAuthor());
                    b.putString("description", book.getDescription());
                    b.putParcelable("image", book.getImage());
                    b.putDouble("ratings", book.getRating());
                    b.putInt("num_ratings", book.getNumRatings());
                    Log.d("blah", "put book in bundle");
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
            Log.d("blah", "exited onPostExecute");
        }
    }


    /**
     * Shows the splash screen over the SearchResultsActivity
     */
    protected void showSplashScreen() {
        mSplashDialog = new Dialog(this);
        mSplashDialog.setContentView(R.layout.loading_screen);
        mSplashDialog.setCancelable(false);
        mSplashDialog.show();

        // Set Runnable to remove splash screen just in case
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                removeSplashScreen();
            }
        }, 20000);
    }

    /**
     * Removes the Dialog that displays the splash screen
     */
    protected void removeSplashScreen() {
        if (mSplashDialog != null) {
            mSplashDialog.dismiss();
            mSplashDialog = null;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        MyActivity.saveLists();
    }
}
