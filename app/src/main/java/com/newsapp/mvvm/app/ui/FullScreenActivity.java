package com.newsapp.mvvm.app.ui;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.databinding.DataBindingUtil;
import com.newsapp.mvvm.app.R;
import com.newsapp.mvvm.app.data.NewsRepository;
import com.newsapp.mvvm.app.databinding.ActivityDetailBinding;
import com.newsapp.mvvm.app.models.Article;

public class FullScreenActivity extends AppCompatActivity {
    public static final String PARAM_ARTICLE = "param-article";
    private ActivityDetailBinding binding;
    private Article article;
    private boolean isSaved;
    private NewsRepository newsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        makeUiFullscreen();
        setupToolbar();
        setupArticleAndListener();
        newsRepository = NewsRepository.getInstance(this);

    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void makeUiFullscreen() {
        // When applying fullscreen layout, transparent bar works only for VERSION < 21
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            binding.getRoot().setFitsSystemWindows(true);
        }
        // Make UI fullscreen and make it load stable
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    /**
     * Extracts Article from Arguments and Adds button listeners
     */
    private void setupArticleAndListener() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(PARAM_ARTICLE)) {
            final Article article = bundle.getParcelable(PARAM_ARTICLE);
            if (article != null) {
                this.article = article;
                binding.setArticle(article);
                //setupShareButton(article);
                setupButtonClickListener(article);
            }
        }
    }

    private void setupButtonClickListener(final Article article) {
        binding.btnReadFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(FullScreenActivity.this, Uri.parse(article.getUrl()));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_enter_transition, R.anim.slide_down_animation);
    }
}