package com.example.isabella.uncommonbooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;




import java.util.ArrayList;


public class SearchResultsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ListView listview = (ListView) findViewById(R.id.results_listview);
        String[] values = new String[]{"Dictionary", "Harry Potter and the Sorcerer's Stone",
                "The Lorax", "The Scarlet Letter", "Ishmael", "Fight Club",
                "Everything's An Argument", "Introduction to Computer Systems"};

        final ArrayList<Book> list = new ArrayList<Book>();
        for (int i = 0; i < values.length; i++) {
            Book newBook = new Book(values[i], "author " + i, 0, "description", 5, 50);
            list.add(newBook);
        }


        final BookViewAdapter adapter = new BookViewAdapter(this,
                R.layout.search_result_item, list);
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
                intent.putExtras(b);
                startActivity(intent);
            }
        });
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
        return super.onOptionsItemSelected(item);
    }
}
