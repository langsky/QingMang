package com.github.langsky.qingmang;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.langsky.qingmang.databinding.RecyclerArticlesBinding;
import com.github.langsky.qingmang.mvp.presenter.ArticleSetPresenter;
import com.github.langsky.qingmang.mvp.view.ArticleSetView;
import com.github.langsky.qingmang.mvp.view.abs.IView;
import com.github.langsky.qingmang.utils.UIUtils;

public class ArticleListActivity extends AppCompatActivity {

    private ArticleSetPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        RecyclerArticlesBinding binding = DataBindingUtil.setContentView(this, R.layout.recycler_articles);
        ArticleSetView view = new ArticleSetView(this, binding);

        getWindow().getDecorView().setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.WHITE);

        presenter = new ArticleSetPresenter(this);
        presenter.setTitle(getIntent().getStringExtra("title"));
        presenter.bindViewModel(view);
        presenter.registerNetData(getIntent().getStringExtra("URL"));
        presenter.registerRxBus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unregisterRxBus();
    }


    public void refresh(View view) {
        presenter.refreshData();
    }


}
