package com.mobileacademy.NewsReader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Valerica Plesu on 4/23/2016.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
//
//    {
//        "by": "rl3",
//            "descendants": 0,
//            "id": 10900279,
//            "score": 3,
//            "time": 1452762088,
//            "title": "Ann Caracristi, who cracked codes, and the glass ceiling, at NSA, dies at 94",
//            "type": "story",
//            "url": "https://www.washingtonpost.com/national/ann-caracristi-who-excelled-at-code-breaking-and-management-dies-at-94/2016/01/11/b8187468-b80d-11e5-b682-4bb4dd403c7d_story.html"
//    }

    private static final String TAG = MyDatabaseHelper.class.getSimpleName();
    public static final String TABLE_ARTICLE = "article";
    public static final String ARTICLE_COLUMN_ID = "id";
    public static final String ARTICLE_COLUMN_TIME = "time";
    public static final String ARTICLE_COLUMN_TITLE = "title";
    public static final String ARTICLE_COLUMN_URL = "url";
    public static final String ARTICLE_PUBLICATION_ID = "pub_id";

    public static final String TABLE_PUBLICATION = "news";
    public static final String PUBLICATION_COLUMN_ID = "id";
    public static final String PUBLICATION_COLUMN_TITLE = "title";
    public static final String PUBLICATION_COLUMN_URL = "url";


    private static final String DATABASE_NAME = "newsreader.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_ARTICLE_CREATE = "create table "
            + TABLE_ARTICLE + "("
            + ARTICLE_COLUMN_ID + " integer primary key not null, "
            + ARTICLE_COLUMN_TIME + " text, "
            + ARTICLE_COLUMN_TITLE + " text not null, "
            + ARTICLE_COLUMN_URL + " text, "
            + ARTICLE_PUBLICATION_ID + " integer not null);";


    private static final String DATABASE_PUBLICATION_CREATE = "create table "
            + TABLE_PUBLICATION + "("
            + PUBLICATION_COLUMN_ID + " integer primary key, "
            + PUBLICATION_COLUMN_TITLE + " text, "
            + PUBLICATION_COLUMN_URL + " text);";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "create " + DATABASE_NAME + " " + DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_ARTICLE_CREATE);
        Log.d(TAG, "create " + DATABASE_ARTICLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        onCreate(db);
    }

}
