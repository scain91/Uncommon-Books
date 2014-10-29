package com.example.isabella.uncommonbooks;

import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Isabella on 10/28/2014.
 */
public class ListOfListsActivity extends Activity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_lists);

        ListView listview = (ListView) findViewById(R.id.list_of_lists_view);
//        String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
//                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
//                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
//                "Android", "iPhone", "WindowsMobile"};


        final ArrayList<BookList> listOfLists = new ArrayList<BookList>();
        listOfLists.add(new BookList("List A"));
        listOfLists.add(new BookList("List B"));
        listOfLists.add(new BookList("List C"));

        for (int i = 0; i < listOfLists.size(); i++) {
            Book newBook = new Book("names", "author", 0, "description", 5, 50);
            Book newBook2 = new Book("names2", "author2", 0, "description", 5, 50);
            Book newBook3 = new Book("names2", "author2", 0, "description", 5, 50);
            listOfLists.get(i).addBook(newBook);
            listOfLists.get(i).addBook(newBook2);
            listOfLists.get(i).addBook(newBook3);
        }


//        final BookViewAdapter adapter = new BookViewAdapter(this,
//                R.layout.list_of_lists, listOfLists);
//        listview.setAdapter(adapter);
        final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.book_list_item, R.id.list_name, listOfLists);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
//                Book item = (Book) parent.getItemAtPosition(position);
//                //make a new activity
//                Intent intent = new Intent(view.getContext(), DetailActivity.class);
//                startActivity(intent);
//                System.out.println("Item Clicked");

            }
        });
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


}


