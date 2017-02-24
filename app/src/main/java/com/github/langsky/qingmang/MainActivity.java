package com.github.langsky.qingmang;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.github.langsky.qingmang.mvp.presenter.MainPagePresenter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    MainPagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPagePresenter(this);
        presenter.init();
    }

    @Override
    protected void onDestroy() {
        presenter.finish();
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.resumeState();
    }

    public void refresh(View view) {
        presenter.refreshState();
    }


}
