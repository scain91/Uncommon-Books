package com.example.isabella.uncommonbooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;


public class MyActivity extends Activity {

    private boolean selected_genres[];
    public static ArrayList<BookList> myLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(true) {    //savedInstanceState == null
            myLists = new ArrayList<BookList>();
            myLists.add(new BookList("Example 1"));
            myLists.add(new BookList("Example 2"));
        }
        else {
            //figure out how to save myLists
        }
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
            Log.d("blah", "starting list of lists activity");
        }
        return super.onOptionsItemSelected(item);
    }

    public void random_button_listener(View v) {
        RandomApiAccess r = new RandomApiAccess();
        //r.execute("9780375753770");
        r.execute();
//        Intent intent = new Intent(this, DetailActivity.class);
//        startActivity(intent);
        System.out.println("In button listener");
    }

    public void search_button_listener(View v) {
        EditText et = (EditText) findViewById(R.id.keywords);
        CheckBox cb = (CheckBox) findViewById(R.id.checkbox_free_ebook);
        boolean ebooks = cb.isChecked();
        if(et != null && et.getText().toString() != null && !et.getText().toString().equals("")){
            String s = et.getText().toString();
            Intent intent = new Intent(this, SearchResultsActivity.class);
            Log.d("blah", "searching for " + s);
            Bundle b = new Bundle();
            b.putString("search", s);
            b.putBoolean("ebooks", ebooks);
            intent.putExtras(b);
            startActivity(intent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter search terms!", Toast.LENGTH_SHORT);
            toast.show();
        }
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

    public class RandomApiAccess extends AsyncTask<Void, Void, Void> {

        ArrayList<Book> book_list = new ArrayList<Book>();

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Log.d("blah", "Just entered doInBackground of ApiAccess");

                final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
                final Books books = new Books.Builder(AndroidHttp.newCompatibleTransport(), jsonFactory, null)
                        .setApplicationName("API Project")
                        .setBooksRequestInitializer(new BooksRequestInitializer(getString(R.string.ANDROID_KEY)))
                        .build();

                int num_books = 0;


                //2 is the number of random books to load at a time; we can change it later
                while(num_books < 2){
                    try{

                            Book b = getValidRandom(books);
                            //if max tries not exceeded. TODO: handle this error
                            if(b != null) {
                                num_books++;
                                book_list.add(b);
                            }
                        }
                        catch(MalformedURLException e){
                            Log.d("Error: ", "bad URL");
                        }
                        catch(IOException e){
                            Log.d("Error: ", "IOException using bitmap");
                        }

                }
              Log.d("blah", "all books added to list");

            } catch (Exception e) {
                Log.d("blah", Log.getStackTraceString(e));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... arg0) {
            Log.d("blah", "entered onProgressUpdate");
        }

        @Override
        protected void onPostExecute(Void a) {

            Log.d("blah", "entered onPostExecute");
            Book book_using = book_list.get(0);
            Intent intent = new Intent(MyActivity.this, DetailActivity.class);
            Bundle b = new Bundle();
            b.putString("title", book_using.getTitle());
            b.putString("author", book_using.getAuthor());
            b.putString("description", book_using.getDescription());
            b.putParcelable("image", book_using.getImage());
            b.putParcelable("thumbnail", book_using.getThumbnail());
            intent.putExtras(b);
            startActivity(intent);
            Log.d("blah", "exited onPostExecute");


        }
    }
    //at some point, make it stop!
    public Book getValidRandom(Books books)throws IOException{
            Log.d("blah", "entered");
            Random r = new Random();
            Bitmap ibmp = null;  //make this a default image in future
            Bitmap tbmp = null; //thumbnail bitmap
            String[] randomWords = getResources().getStringArray(R.array.random_query_words);
            int max_index = randomWords.length - 1;
            int api_calls = 0;
            int max_calls = 3;
            while(api_calls < max_calls) {
                Books.Volumes.List volume_request = books.volumes().list(randomWords[r.nextInt(max_index)]);
                Volumes volumes = volume_request.execute();
                if(volumes != null && volumes.getTotalItems() > 0){
                    for(Volume volume: volumes.getItems()) {
                        Volume.VolumeInfo volumeInfo = volume.getVolumeInfo();
                        if (volumeInfo == null || volumeInfo.getTitle() == null ||
                                volumeInfo.getDescription() == null ||
                                volumeInfo.getAverageRating() == null || volumeInfo.getRatingsCount() == null
                                || volumeInfo.getImageLinks() == null || volumeInfo.getImageLinks().getThumbnail() == null
                                || volumeInfo.getImageLinks().getSmallThumbnail() == null) {
                            Log.d("blah", "Continuing");
                        } else {
                            String author = volumeInfo.getAuthors() != null ?
                                    volumeInfo.getAuthors().toString() : "authors missing";
                            //removing brackets
                            author = author.substring(1, author.length() - 1);
                            //setting up large image
                            String imageString = volumeInfo.getImageLinks().getThumbnail();
                            URL imgUrl = new URL(imageString);
                            ibmp = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
                            //setting up smaller image
                            String thumbString = volumeInfo.getImageLinks().getSmallThumbnail();
                            URL thumbUrl = new URL(thumbString);
                            tbmp = BitmapFactory.decodeStream(thumbUrl.openConnection().getInputStream());
                            Log.d("blah", "Num Tries:" + String.valueOf(api_calls));
                            Log.d("blah", volumeInfo.getTitle());
                            Book b = new Book(volumeInfo.getTitle(), author, ibmp, tbmp, volumeInfo.getDescription(), volumeInfo.getAverageRating(), volumeInfo.getRatingsCount());
                            return b;
                        }
                    }

                }
                Log.d("blah", "adding after continuing");
                api_calls++;
            }
            return null;
    }

}
