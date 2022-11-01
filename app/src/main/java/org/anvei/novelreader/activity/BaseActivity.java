package org.anvei.novelreader.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public static final String EXTRA_NOVEL_URL = "org.anvei.novel.url";

    public static final String EXTRA_NOVEL_NAME = "org.anvei.novel.name";

    public static final String EXTRA_SEARCH_KEYWORD = "org.anvei.search.keyword";

    public static final String DATABASE_NAME = "anvei";

    public static final String TABLE_WEB_CACHE = "WebCache";

    public static final String TABLE_HISTORY = "History";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
