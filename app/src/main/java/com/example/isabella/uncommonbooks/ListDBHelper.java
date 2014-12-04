package com.example.isabella.uncommonbooks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ListDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "list8";

    public static final String LIST_NAME_COL = "list_name";
    public static final String BOOK_TITLE_COL = "book_title";
    public static final String BOOK_AUTHOR_COL = "book_author";
    public static final String BOOK_DESCR_COL = "book_descr";
    public static final String BOOK_IMAGE_COL = "book_image";
    public static final String BOOK_THUMBNAIL_COL = "book_thumb";
    public static final String BOOK_RATINGS_COL = "book_rating";
    public static final String BOOK_NUM_RATINGS_COL = "book_num_rating";

    ListDBHelper(Context context) {
        super(context, "location8.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME
                +" ("+LIST_NAME_COL+" varchar(100), "+BOOK_TITLE_COL+" varchar(50)"
                +", "+BOOK_AUTHOR_COL+" varchar(50), "+BOOK_IMAGE_COL+" blob"
                +", "+BOOK_THUMBNAIL_COL+" blob, "+BOOK_DESCR_COL+" varchar(1000)"
                +", "+BOOK_RATINGS_COL+" decimal(1,1), "+BOOK_NUM_RATINGS_COL+" integer,"+
                " PRIMARY KEY("+LIST_NAME_COL+", "+BOOK_TITLE_COL+", "+BOOK_AUTHOR_COL+", "+BOOK_DESCR_COL+"));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }


}
