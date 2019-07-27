package com.newsapp.mvvm.app.ui;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.newsapp.mvvm.app.R;
import com.newsapp.mvvm.app.databinding.ActivityMainBinding;
import com.newsapp.mvvm.app.service.NewsServiceClient;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupToolbar();
        NewsFragment newsFragment = NewsFragment.newInstance(NewsServiceClient.Category.technology);
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, newsFragment)
                .commit();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
            //Remove trailing space from toolbar
            binding.toolbar.setContentInsetsAbsolute(10, 10);
        }
    }
}
