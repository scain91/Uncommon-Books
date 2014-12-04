package com.example.isabella.uncommonbooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;


public class DetailActivity extends Activity {
    private ArrayList<BookList> mList;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            book = new Book(b.getString("title"), b.getString("author"), (Bitmap) b.getParcelable("image"),
                    null, b.getString("description"), b.getDouble("ratings"), b.getInt("num_ratings"));
            Log.d("blah", "reconstructed book from intent");

            TextView txtTitle = (TextView) findViewById(R.id.title_book);
            TextView txtAuthor = (TextView) findViewById(R.id.author_book);
            TextView txtDescription = (TextView) findViewById(R.id.description_book);
            TextView txtRatings = (TextView) findViewById(R.id.ratings_book);
            ImageView img = (ImageView) findViewById(R.id.image_book);
            txtTitle.setText(book.getTitle());
            txtAuthor.setText(book.getAuthor());
            txtDescription.setText(book.getDescription());
            String rating = book.getRating() != -1? String.valueOf(book.getRating()): "Unavailable";
            String num_ratings = book.getNumRatings() != -1? String.valueOf(book.getNumRatings()): "Unavailable";
            txtRatings.setText("Rating: "+rating+" Num. Ratings: "+num_ratings);
            img.setImageBitmap(book.getImage());
            //Currently works

            img.setImageBitmap((Bitmap) b.getParcelable("image"));
            //Trying to make image loading an asynchronous task
            //new DownloadImageTask(img).execute(b.getString("image"));


        }
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
        else if(id == R.id.view_lists){
            Intent intent = new Intent(this, ListOfListsActivity.class);
            startActivity(intent);
            System.out.println("Got past startActivity");
        }
        return super.onOptionsItemSelected(item);
    }

    public void addBookToList(View v) {
        String[] u = new String[MyActivity.myLists.size() + 1];
        for (int i = 0; i < MyActivity.myLists.size(); i++)
            u[i] = MyActivity.myLists.get(i).getName();
        u[MyActivity.myLists.size()] = "New list...";
        final String[] myListsNames = u;

        AlertDialog.Builder listPopUp = new AlertDialog.Builder(this);
        listPopUp.setTitle(R.string.choose_list);
        listPopUp.setItems(myListsNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("blah", "trying to add");

                //Add to user's lists
                if(whichButton == MyActivity.myLists.size()){
                    Log.d("blah", "adding new list");
                    addNewList();
                }
                else if (MyActivity.myLists.get(whichButton).getBooks().contains(book)) {
                    Log.d("blah", "tried to add book to list it's already in.");
                    Toast toast = Toast.makeText(getApplicationContext(), book.getTitle() +
                            " is already in " + myListsNames[whichButton], Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    MyActivity.myLists.get(whichButton).addBook(book);
                    Log.d("blah", "adding " + book.getTitle() + " to " + myListsNames[whichButton]);
                    Toast toast = Toast.makeText(getApplicationContext(), "Adding " + book.getTitle() +
                            " to " + myListsNames[whichButton], Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        listPopUp.show();
    }

    public void addNewList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BookList bl = new BookList(input.getText().toString());
                bl.addBook(book);
                MyActivity.myLists.add(bl);
                Log.d("blah", "new_name set to " + input.getText().toString());
                Toast toast = Toast.makeText(getApplicationContext(), "Adding " + book.getTitle() +
                        " to new list " + bl.getName(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setTitle("New book list");
        builder.show();
    }

    @Override
    protected void onStop(){
        super.onStop();
        MyActivity.saveLists();
    }

}
