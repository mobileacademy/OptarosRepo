package com.mobileacademy.NewsReader.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by danielastamati on 15/04/16.
 */
public class Publication implements Serializable {


    private String name;
    private int pictureResource;
    private ArrayList<Article> articleList;

    public Publication(String name, int pictureResource) {
        this.name = name;
        this.pictureResource = pictureResource;

    }

    public ArrayList<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(ArrayList<Article> articleList) {
        this.articleList = articleList;
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
}
