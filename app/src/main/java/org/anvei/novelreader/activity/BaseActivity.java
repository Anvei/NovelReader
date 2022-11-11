package org.anvei.novelreader.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.anvei.novelreader.AppDatabase;

public class BaseActivity extends AppCompatActivity {

    protected static final String EXTRA_NOVEL_INFO = "org.anvei.novel.info";

    protected static final String EXTRA_NOVEL_POSITION = "org.anvei.novel.position";

    protected static final String EXTRA_SEARCH_KEYWORD = "org.anvei.search.keyword";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public AppDatabase getAppDatabase() {
        return AppDatabase.getInstance(this);
    }
}
