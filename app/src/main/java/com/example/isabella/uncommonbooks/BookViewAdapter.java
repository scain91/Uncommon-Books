package com.example.isabella.uncommonbooks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class BookViewAdapter extends ArrayAdapter<Book> {

    Context context;

    public BookViewAdapter(Context context, int resourceId, List<Book> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtAuthor;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Book book = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.search_result_item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.firstLine);
            holder.txtAuthor = (TextView) convertView.findViewById(R.id.secondLine);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.txtAuthor.setText(book.getAuthor());
        holder.txtTitle.setText(book.getTitle());

        //Trying to make image loading an asynchronous task
        //new DownloadImageTask(holder.imageView).execute(book.getImage());


        holder.imageView.setImageBitmap(book.getImage());

        return convertView;
    }
}