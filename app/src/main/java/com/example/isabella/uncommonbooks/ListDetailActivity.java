package com.example.isabella.uncommonbooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import android.app.Activity;
import android.widget.TextView;

/**
 * Created by Isabella on 10/28/2014.
 */
public class ListDetailActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        ListView listview = (ListView) findViewById(R.id.results_listview);

        Intent i = getIntent();
        BookList bookList = (BookList) i.getSerializableExtra("bookList");
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
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }


}