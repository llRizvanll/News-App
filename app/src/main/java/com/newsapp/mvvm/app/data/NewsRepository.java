package com.newsapp.mvvm.app.data;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.newsapp.mvvm.app.models.Article;
import com.newsapp.mvvm.app.models.Source;
import com.newsapp.mvvm.app.models.Specification;
import com.newsapp.mvvm.app.service.NewsApiClient;
import com.newsapp.mvvm.app.utils.AppExecutors;

import java.util.List;

public class NewsRepository {
    private static final Object LOCK = new Object();
    private static NewsRepository sInstance;

    private final NewsApiClient newsApiService;
    private final HeadlinesDao headlinesDao;
    private final SourcesDao sourcesDao;
    private final AppExecutors mExecutor;
    private final MutableLiveData<List<Article>> networkArticleLiveData;
    private final MutableLiveData<List<Source>> networkSourceLiveData;

    // required private constructor for Singleton pattern
    private NewsRepository(Context context) {
        newsApiService = NewsApiClient.getInstance(context);
        headlinesDao = NewsDatabase.getInstance(context).headlinesDao();
        sourcesDao = NewsDatabase.getInstance(context).sourcesDao();
        mExecutor = AppExecutors.getInstance();
        networkArticleLiveData = new MutableLiveData<>();
        networkSourceLiveData = new MutableLiveData<>();
        networkArticleLiveData.observeForever(new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable final List<Article> articles) {
                if (articles != null) {
                    mExecutor.getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            headlinesDao.bulkInsert(articles);
                        }
                    });
                }
            }
        });
        networkSourceLiveData.observeForever(new Observer<List<Source>>() {
            @Override
            public void onChanged(@Nullable final List<Source> sources) {
                if (sources != null) {
                    mExecutor.getDiskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            sourcesDao.bulkInsert(sources);
                        }
                    });
                }
            }
        });
    }

    public synchronized static NewsRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new NewsRepository(context);
            }
        }
        return sInstance;
    }

    public LiveData<List<Article>> getHeadlines(final Specification specs) {
        final LiveData<List<Article>> networkData = newsApiService.getHeadlines(specs);
        networkData.observeForever(new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                if (articles != null) {
                    networkArticleLiveData.setValue(articles);
                    networkData.removeObserver(this);
                }
            }
        });
        return headlinesDao.getArticleByCategory(specs.getCategory());
    }

    public LiveData<List<Source>> getSources(final Specification specs) {
        final LiveData<List<Source>> networkData = newsApiService.getSources(specs);
        networkData.observeForever(new Observer<List<Source>>() {
            @Override
            public void onChanged(@Nullable List<Source> sources) {
                if (sources != null) {
                    networkSourceLiveData.setValue(sources);
                    networkData.removeObserver(this);
                }
            }
        });
        return sourcesDao.getAllSources();
    }


}