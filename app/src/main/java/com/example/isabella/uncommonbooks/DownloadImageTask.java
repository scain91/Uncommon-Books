package com.example.isabella.uncommonbooks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Basil on 11/23/2014.
 * Heavy references to http://stackoverflow.com/questions/5776851/load-image-from-url
 */
/*
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            URL url = new URL(urldisplay);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        catch (Exception e) {
            Log.e("Error", "something went wrong with translating the URL into a bitmap");
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        try {
            bmImage.setImageBitmap(result);
        }
        catch(Exception e){
            Log.e("Error", "failed to set bitmap");
            bmImage.setImageResource(R.drawable.argument);
        }
    }
}
*/