package com.newsapp.mvvm.app.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.newsapp.mvvm.app.models.Source;

import java.util.List;

@Dao
public interface SourcesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void bulkInsert(List<Source> sources);

    @Query("SELECT * FROM sources")
    LiveData<List<Source>> getAllSources();
}
