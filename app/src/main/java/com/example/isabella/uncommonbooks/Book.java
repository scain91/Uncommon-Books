package com.example.isabella.uncommonbooks;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Book{

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Bitmap getImage() {
        return image;
    }

    public Bitmap getThumbnail(){ return thumbnail; }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Book))
            return false;
        Book b = (Book) obj;
        return b.getTitle().equals(this.title) &&
                b.getAuthor().equals(this.author) &&
                b.getDescription().equals(this.description) &&
                b.getRating() == rating && b.getNumRatings() == numRatings;
    }

    private String title, author, description;
    private Bitmap image, thumbnail;
    private double rating;
    private int numRatings;

    public Book(String title, String author, Bitmap image, Bitmap thumbnail, String d, double r, int nR){
        this.title = title;
        this.author = author;
        this.image = image;
        this.thumbnail = thumbnail;
        description = d;
        rating = r;
        numRatings = nR;
    }

    public boolean equals(Book book2){
        return this.title.equals(book2.title) && this.author.equals(book2.author) && this.description.equals(book2.description);
    }


}
