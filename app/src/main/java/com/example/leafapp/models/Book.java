package com.example.leafapp.models;

import java.util.List;

public class Book {

    private String image;
    private List<String> title;
    private List<String> authers;
    private String isbn;
    private String pub;
    private String lang;
    private String year;

    public Book() {
    }

    public Book(String image, List<String> title, List<String> authers, String isbn, String pub, String lang, String year) {
        this.image = image;
        this.title = title;
        this.authers = authers;
        this.isbn = isbn;
        this.pub = pub;
        this.lang = lang;
        this.year = year;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getAuthers() {
        return authers;
    }

    public void setAuthers(List<String> authers) {
        this.authers = authers;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
