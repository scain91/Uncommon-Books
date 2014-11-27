package com.example.isabella.uncommonbooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
            ImageView img = (ImageView) findViewById(R.id.image_book);
            txtTitle.setText(book.getTitle());
            txtAuthor.setText(book.getAuthor());
            txtDescription.setText(book.getDescription());
            //Currently works
            img.setImageBitmap(book.getImage());
        }
    }

    public void addBookToList(View v) {
        String[] u = new String[MyActivity.myLists.size()];
        for (int i = 0; i < u.length; i++)
            u[i] = MyActivity.myLists.get(i).getName();
        final String[] myListsNames = u;

        AlertDialog.Builder listPopUp = new AlertDialog.Builder(this);
        listPopUp.setTitle(R.string.choose_list);
        listPopUp.setItems(myListsNames, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d("blah", "trying to add");

                //Add to user's lists
                if (MyActivity.myLists.get(whichButton).getBooks().contains(book)) {
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
}
