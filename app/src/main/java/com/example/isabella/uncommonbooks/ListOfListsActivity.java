package com.example.isabella.uncommonbooks;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.*;
import java.util.Iterator;

import java.util.ArrayList;


public class ListOfListsActivity extends Activity {
    protected static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_lists);
        active = true;
        ListView listview = (ListView) findViewById(R.id.list_of_lists_view);

        final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.book_list_item,
                R.id.list_name, MyActivity.myLists);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                BookList bookList = (BookList) parent.getItemAtPosition(position);
                //make a new activity
                Intent intent = new Intent(view.getContext(), ListDetailActivity.class);
                Bundle b = new Bundle();
                b.putInt("book list index", position);
                intent.putExtras(b);
                startActivity(intent);
                System.out.println("Item Clicked");
            }
        });
    }

    public void addNewList(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyActivity.myLists.add(new BookList(input.getText().toString()));
                ListView listview = (ListView) findViewById(R.id.list_of_lists_view);
                final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                        R.layout.book_list_item, R.id.list_name, MyActivity.myLists);
                listview.setAdapter(adapter);
                Log.d("blah", "new_name set to " + input.getText().toString());
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

    protected void onStop() {
        super.onStop();
        active = false;
        MyActivity.saveLists();

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
}