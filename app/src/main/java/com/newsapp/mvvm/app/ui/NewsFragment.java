package com.newsapp.mvvm.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapp.mvvm.app.R;
import com.newsapp.mvvm.app.adapters.NewsAdapter;
import com.newsapp.mvvm.app.databinding.NewsFragmentBinding;
import com.newsapp.mvvm.app.models.Article;
import com.newsapp.mvvm.app.models.Specification;
import com.newsapp.mvvm.app.service.NewsServiceClient;

import java.util.List;

import timber.log.Timber;

public class NewsFragment extends Fragment implements NewsAdapter.NewsAdapterListener {
    public static final String PARAM_CATEGORY = "param-category";
    public static final String PARAM_LIST_STATE = "param-state";
    private final NewsAdapter newsAdapter = new NewsAdapter(null, this);
    private NewsServiceClient.Category newsCategory;
    private NewsFragmentBinding binding;
    private boolean showSaved = false;
    private Parcelable listState;

    public static NewsFragment newInstance(NewsServiceClient.Category category) {
        NewsFragment fragment = new NewsFragment();
        if (category == null) {
            return fragment;
        }
        Bundle args = new Bundle();
        args.putString(PARAM_CATEGORY, category.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsCategory = NewsServiceClient.Category
                    .valueOf(getArguments().getString(PARAM_CATEGORY));
        } else {
            showSaved = true;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.news_fragment, container, false);
        RecyclerView recyclerView = binding.rvNewsPosts;
        recyclerView.setAdapter(newsAdapter);
        if (getContext() != null) {
            DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            divider.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));
            recyclerView.addItemDecoration(divider);
        }

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(PARAM_LIST_STATE);
        }
        NewsViewModel viewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
            Specification specs = new Specification();
            specs.setCategory(newsCategory);
            viewModel.getNewsHeadlines(specs).observe(this, new Observer<List<Article>>() {
                @Override
                public void onChanged(@Nullable List<Article> articles) {
                    if (articles != null) {
                        newsAdapter.setArticles(articles);
                        restoreRecyclerViewState();
                    }
                }
            });
    }

    @Override
    public void onNewsItemClicked(Article article) {
        Timber.d("Recieved article");
        Intent intent = new Intent(getContext(), FullScreenActivity.class);
        intent.putExtra(FullScreenActivity.PARAM_ARTICLE, article);
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        binding.rvNewsPosts.setLayoutAnimation(controller);
        binding.rvNewsPosts.scheduleLayoutAnimation();
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().overridePendingTransition(R.anim.slide_up_animation, R.anim.fade_exit_transition);
        }
    }

    @Override
    public void onItemOptionsClicked(Article article) { }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (binding.rvNewsPosts.getLayoutManager() != null) {
            listState = binding.rvNewsPosts.getLayoutManager().onSaveInstanceState();
            outState.putParcelable(PARAM_LIST_STATE, listState);
        }
    }

    private void restoreRecyclerViewState() {
        if (binding.rvNewsPosts.getLayoutManager() != null) {
            binding.rvNewsPosts.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
