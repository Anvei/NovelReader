package org.anvei.novelreader.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected static final String EXTRA_SEARCH_KEYWORD = "org.anvei.search.keyword";

    protected static final String EXTRA_NOVEL_HOME_NAME = "org.anvei.novel.home.name";

    protected static final String EXTRA_NOVEL_HOME_AUTHOR = "org.anvei.novel.home.author";

    protected static final String EXTRA_NOVEL_HOME_URL = "org.anvei.novel.home.url";

    protected static final String EXTRA_NOVEL_HOME_COVER = "org.anvei.novel.home.cover";

    protected static final String EXTRA_NOVEL_HOME_BRIEF = "org.anvei.novel.home.brief";

    protected static final String EXTRA_NOVEL_HOME_WEBSITE = "org.anvei.novel.home.website";

    public static final String DATABASE_NAME = "anvei";

    public static final String TABLE_WEB_CACHE = "WebCache";

    public static final String TABLE_HISTORY = "History";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
