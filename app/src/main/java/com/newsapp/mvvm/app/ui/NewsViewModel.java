package com.newsapp.mvvm.app.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.newsapp.mvvm.app.data.NewsRepository;
import com.newsapp.mvvm.app.models.Article;
import com.newsapp.mvvm.app.models.Specification;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    private final NewsRepository newsRepository;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        newsRepository = NewsRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Article>> getNewsHeadlines(Specification specification) {
        return newsRepository.getHeadlines(specification);
    }

}
