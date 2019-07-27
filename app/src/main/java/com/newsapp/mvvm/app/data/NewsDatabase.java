package com.newsapp.mvvm.app.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.newsapp.mvvm.app.models.Article;
import com.newsapp.mvvm.app.models.Source;

@Database(entities = {Article.class, Source.class},
        version = 1,
        exportSchema = false)
@TypeConverters(DatabaseConverters.class)
public abstract class NewsDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "news_app";
    private static NewsDatabase sInstance;

    public static NewsDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        NewsDatabase.class,
                        DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract HeadlinesDao headlinesDao();

    public abstract SourcesDao sourcesDao();

}
