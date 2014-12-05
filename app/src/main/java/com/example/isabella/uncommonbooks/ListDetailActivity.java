package com.example.isabella.uncommonbooks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import android.app.Activity;
import android.widget.TextView;

public class ListDetailActivity extends Activity{

    private BookList bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        ListView listview = (ListView) findViewById(R.id.results_listview);
        if(getIntent() != null && getIntent().getExtras() != null) {
            bookList = MyActivity.myLists.get(getIntent().getExtras().getInt("book list index"));
        }
        TextView list_title = (TextView) findViewById(R.id.book_list_title);
        list_title.setText(bookList.getName());

        final BookViewAdapter adapter = new BookViewAdapter(this,
                R.layout.search_result_item, bookList.getBooks());
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Book book = (Book) parent.getItemAtPosition(position);
                //make a new activity
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                Bundle b = new Bundle();

                b.putString("title", book.getTitle());
                b.putString("author", book.getAuthor());
                b.putString("description", book.getDescription());
                b.putParcelable("image", book.getImage());
                b.putDouble("ratings", book.getRating());
                b.putInt("num_ratings", book.getNumRatings());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
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
        if(id == R.id.view_home) {
            Intent intent = new Intent(this, MyActivity.class);
            startActivity(intent);
            Log.d("blah", "starting MyActivity");
        }
        else if(id == R.id.view_lists){
            Intent intent = new Intent(this, ListOfListsActivity.class);
            startActivity(intent);
            System.out.println("Got past startActivity");
        }
        return super.onOptionsItemSelected(item);
    }



}
