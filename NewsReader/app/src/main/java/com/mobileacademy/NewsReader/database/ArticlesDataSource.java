package com.mobileacademy.NewsReader.database;

/**
 * Created by Valerica Plesu on 4/23/2016.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mobileacademy.NewsReader.models.Article;

public class ArticlesDataSource {

//    {
//            "by": "rl3",
//            "descendants": 0,
//            "id": 10900279,
//            "score": 3,
//            "time": 1452762088,
//            "title": "Ann Caracristi, who cracked codes, and the glass ceiling, at NSA, dies at 94",
//            "type": "story",
//            "url": "https://www.washingtonpost.com/national/ann-caracristi-who-excelled-at-code-breaking-and-management-dies-at-94/2016/01/11/b8187468-b80d-11e5-b682-4bb4dd403c7d_story.html"
//    }


    private static final String TAG = ArticlesDataSource.class.getSimpleName();
    // Database fields
    private SQLiteDatabase database;
    private MyDatabaseHelper dbHelper;

    private String[] allColumns = { MyDatabaseHelper.ARTICLE_COLUMN_ID,
            MyDatabaseHelper.ARTICLE_COLUMN_TITLE, MyDatabaseHelper.ARTICLE_COLUMN_TIME,
            MyDatabaseHelper.ARTICLE_COLUMN_URL, MyDatabaseHelper.ARTICLE_PUBLICATION_ID};

    public ArticlesDataSource(Context context) {
        dbHelper = new MyDatabaseHelper(context);
    }

    public void open() throws SQLException {
        Log.d(TAG, "open database");
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Article createArticle(int id, String title, String time, String url, int publicationId) {

        ContentValues values = new ContentValues();
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_ID, id);
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_TITLE, title);
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_TIME, time);
        values.put(MyDatabaseHelper.ARTICLE_COLUMN_URL, url);
        values.put(MyDatabaseHelper.ARTICLE_PUBLICATION_ID, publicationId);


        // insert or replace method
        long insertId = database.insertWithOnConflict(MyDatabaseHelper.TABLE_ARTICLE, null,
                values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG, "insertId, " + insertId);

        // query database
        Cursor cursor = database.query(MyDatabaseHelper.TABLE_ARTICLE,
                allColumns, MyDatabaseHelper.ARTICLE_COLUMN_ID + " = " + id, null,
                null, null, null);

        // move cursor to the first row
        cursor.moveToFirst();


        Article article = cursorToArticle(cursor);

        // !!!! close cursor
        cursor.close();

        return article;
    }

    public void deleteArticle(Article article) {
        int id = article.getId();
        System.out.println("Article deleted with id: " + id);
        database.delete(MyDatabaseHelper.TABLE_ARTICLE, MyDatabaseHelper.ARTICLE_COLUMN_ID
                + " = " + id, null);
    }

    public List<Article> getAllArticles() {
        List<Article> comments = new ArrayList<Article>();

        Cursor cursor = database.query(MyDatabaseHelper.TABLE_ARTICLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Article comment = cursorToArticle(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }


        // !!! make sure to close the cursor
        cursor.close();

        return comments;
    }

    public List<Article> getAllArticlesByPublication(int publicationId) {
        List<Article> articles = new ArrayList<Article>();

        Cursor cursor = database.query(MyDatabaseHelper.TABLE_ARTICLE,
                allColumns,  MyDatabaseHelper.ARTICLE_PUBLICATION_ID + " = " + publicationId, null, null, null, null);

        cursor.moveToFirst();
        Log.d(TAG, "cursor size: " + cursor.getCount());

        while (!cursor.isAfterLast()) {
            Article article = cursorToArticle(cursor);
            articles.add(article);
            cursor.moveToNext();
        }


        // !!! make sure to close the cursor
        cursor.close();

        return articles;
    }

    private Article cursorToArticle(Cursor cursor) {
        Article article = new Article();

        article.setId(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.ARTICLE_COLUMN_ID)));
        article.setName(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.ARTICLE_COLUMN_TITLE)));
        article.setTime(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.ARTICLE_COLUMN_TIME)));
        article.setUrl(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.ARTICLE_COLUMN_URL)));
        article.setPublicationId(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.ARTICLE_PUBLICATION_ID)));

        return article;
    }
}

