package com.example.isabella.uncommonbooks;

import java.util.ArrayList;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Bundle;

/**
 * Created by Isabella on 10/28/2014.
 */
public class BookList{

    private String name;
    private ArrayList<Book> books = new ArrayList<Book>();

    public BookList(String listName){
        name = listName;
    }

    public void addBook(Book book){
        books.add(book);
    }
    public boolean removeBook(Book book){
        return books.remove(book);
    }

    public String toString(){
        return name;
    }

    public ArrayList getBooks(){
        return books;
    }

    public String getName(){return name;}

}
