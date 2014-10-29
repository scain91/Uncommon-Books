package com.example.isabella.uncommonbooks;

import java.util.ArrayList;

/**
 * Created by Isabella on 10/28/2014.
 */
public class BookList {

    private String name;
    private ArrayList<Book> books = new ArrayList<Book>();

    public BookList(String listName){
        name = listName;
    }

    public void addBook(Book book){
        books.add(book);
    }
    public void removeBook(Book book){
        books.remove(book);
    }

    public String toString(){
        return name;
    }
}
