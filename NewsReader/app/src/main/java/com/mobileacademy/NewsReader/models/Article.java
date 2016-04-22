package com.mobileacademy.NewsReader.models;

/**
 * Created by danielastamati on 21/04/16.
 */
public class Article {
    private String name;
    private int pictureResource;

    public Article(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPictureResource() {
        return pictureResource;
    }

    public void setPictureResource(int pictureResource) {
        this.pictureResource = pictureResource;
    }

    @Override
    public String toString() {
        return name;
    }
}
