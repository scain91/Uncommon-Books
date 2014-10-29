package com.example.isabella.uncommonbooks;

public class Book {
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    private String title, author, description;
    private double rating;
    private int numRatings, image;

    public Book(String title, String author, int image, String d, double r, int nR){
        this.title = title;
        this.author = author;
        this.image = image;
        description = d;
        rating = r;
        numRatings = nR;
    }


}
