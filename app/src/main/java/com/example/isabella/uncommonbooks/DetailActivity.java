package com.example.isabella.uncommonbooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);


        //If coming from results list
        if(getIntent() != null && getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            TextView txtTitle = (TextView) findViewById(R.id.title_book);
            TextView txtAuthor = (TextView) findViewById(R.id.author_book);
            TextView txtDescription = (TextView) findViewById(R.id.description_book);
            ImageView img = (ImageView) findViewById(R.id.image_book);
            txtTitle.setText(b.getString("title"));
            txtAuthor.setText(b.getString("author"));
            txtDescription.setText(b.getString("description"));

            //Currently works
            img.setImageBitmap((Bitmap) b.getParcelable("image"));
            //Trying to make image loading an asynchronous task
            //new DownloadImageTask(img).execute(b.getString("image"));
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


    public void addBookToList(View v) {

        //This array should be a global variable of the user's lists that the whole app can access
        final String user_lists[] = {"List A", "List B", "List C"};
        boolean nothing[] = new boolean[3];
        AlertDialog.Builder listPopUp = new AlertDialog.Builder(this);
        listPopUp.setTitle(R.string.choose_list);
        listPopUp.setItems(user_lists, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Add to user's lists
                Toast toast = Toast.makeText(getApplicationContext(), "Book added to " + user_lists[whichButton], Toast.LENGTH_SHORT);
                toast.show();
                Toast t = Toast.makeText(getApplicationContext(), "Just kidding, we haven't implemented that yet...", Toast.LENGTH_SHORT);
                t.show();
            }
        });
        listPopUp.show();
    }
}

