package com.example.isabella.uncommonbooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MyActivity extends Activity {

    private boolean selected_genres[];
    public static ArrayList<BookList> myLists;

    public static final int MIN_NUM_RATINGS = 5;
    public static final int MAX_NUM_RATINGS = 50;
    public static final double MIN_AVG_RATING = 3.0;

    protected Dialog mSplashDialog;

    public static ListDBHelper helper;
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myLists = new ArrayList<BookList>();
        helper = new ListDBHelper(this);
        db = helper.getWritableDatabase();
        retrieveLists();
        setContentView(R.layout.activity_my);
        //Grab genres from Google and set the size of selected_genres to the number we find
        selected_genres = new boolean[6];
        System.out.println("Created");
    }

    public void retrieveLists() {
        Cursor table = MyActivity.db.rawQuery("Select * from " + ListDBHelper.TABLE_NAME, null);
        String list_name = "";
        Log.d("blah", "L: " + MyActivity.myLists.toString());
        BookList book_list;
        if (table.moveToFirst()) {
            while (table.moveToNext()) {
                list_name = table.getString(0);
                book_list = new BookList(list_name);
                Log.d("blah", "List: " + list_name);
                boolean new_list = true;
                for (BookList b : MyActivity.myLists) {
                    if (b.getName().equals(list_name)) {
                        new_list = false;
                        book_list = b;
                        break;
                    }
                }
                if (new_list) {
                    MyActivity.myLists.add(book_list);
                }
                if (!table.getString(1).equals("") && !table.getString(2).equals("")) {
                    Log.d("blah", "Not null");
                    byte[] image_rep = table.getBlob(3);
                    byte[] thumbnail_rep = table.getBlob(4);
                    Book book = new Book(table.getString(1), table.getString(2), BitmapFactory.decodeByteArray(image_rep, 0, image_rep.length), BitmapFactory.decodeByteArray(thumbnail_rep, 0, thumbnail_rep.length), table.getString(5), table.getDouble(6), table.getInt(7));
                    ArrayList<Book> books = book_list.getBooks();
                    if (!books.contains(book))
                        book_list.addBook(book);
                }
            }
            Log.d("blah", MyActivity.myLists.toString());

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
            Log.d("blah", "starting list of lists activity");
        }
        return super.onOptionsItemSelected(item);
    }

    public void random_button_listener(View v) {
        RandomApiAccess r = new RandomApiAccess();
        showSplashScreen();
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
                while(num_books < 1){
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

            removeSplashScreen();
            Log.d("blah", "entered onPostExecute");
            Book book_using = book_list.get(0);
            Intent intent = new Intent(MyActivity.this, DetailActivity.class);
            Bundle b = new Bundle();
            b.putString("title", book_using.getTitle());
            b.putString("author", book_using.getAuthor());
            b.putString("description", book_using.getDescription());
            b.putParcelable("image", book_using.getImage());
            b.putParcelable("thumbnail", book_using.getThumbnail());
            b.putDouble("ratings", book_using.getRating());
            b.putInt("num_ratings", book_using.getNumRatings());
            intent.putExtras(b);
            startActivity(intent);
            Log.d("blah", "exited onPostExecute");


        }
    }
    @Override
    protected void onStop() {

        super.onStop();
        saveLists();

    }
    public static void saveLists() {
        ContentValues columns = new ContentValues();
//        columns.put(ListDBHelper.LIST_NAME_COL, "bl");
        Log.d("blah", "stop");

        for (BookList list : myLists) {
            ArrayList<Book> books = list.getBooks();
            columns.put(ListDBHelper.LIST_NAME_COL, list.getName());
            if (books.size() == 0) {
                columns.put(ListDBHelper.BOOK_TITLE_COL, "");
                columns.put(ListDBHelper.BOOK_AUTHOR_COL, "");
                columns.put(ListDBHelper.BOOK_DESCR_COL, "");
                columns.put(ListDBHelper.BOOK_IMAGE_COL, "");
                columns.put(ListDBHelper.BOOK_THUMBNAIL_COL, "");
                columns.put(ListDBHelper.BOOK_RATINGS_COL, "");
                columns.put(ListDBHelper.BOOK_NUM_RATINGS_COL, "");
            } else {
                for (Book book : books) {
                    columns.put(ListDBHelper.BOOK_TITLE_COL, book.getTitle());
                    columns.put(ListDBHelper.BOOK_AUTHOR_COL, book.getAuthor());
                    columns.put(ListDBHelper.BOOK_DESCR_COL, book.getDescription());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    book.getImage().compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                    columns.put(ListDBHelper.BOOK_IMAGE_COL, outputStream.toByteArray());
                    ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
                    book.getImage().compress(Bitmap.CompressFormat.PNG, 0, outputStream2);
                    columns.put(ListDBHelper.BOOK_THUMBNAIL_COL, outputStream2.toByteArray());
                    columns.put(ListDBHelper.BOOK_RATINGS_COL, book.getRating());
                    columns.put(ListDBHelper.BOOK_NUM_RATINGS_COL, book.getNumRatings());

                }
            }
            Long row_id = db.insert(ListDBHelper.TABLE_NAME, null, columns);
            columns.clear();
            Log.d("blah", "Row: " + String.valueOf(row_id));
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
                        if (!(volumeInfo == null || volumeInfo.getTitle() == null ||
                                volumeInfo.getDescription() == null ||
                                volumeInfo.getAverageRating() == null || volumeInfo.getAverageRating() < MIN_AVG_RATING
                                || volumeInfo.getRatingsCount() == null || volumeInfo.getRatingsCount() < MIN_NUM_RATINGS
                                || volumeInfo.getRatingsCount() > MAX_NUM_RATINGS
                                || volumeInfo.getImageLinks() == null || volumeInfo.getImageLinks().getThumbnail() == null
                                || volumeInfo.getImageLinks().getSmallThumbnail() == null)) {

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

}
