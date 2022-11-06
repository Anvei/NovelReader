package org.anvei.novelreader.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected static final String EXTRA_NOVEL_INFO = "org.anvei.novel.info";

    protected static final String EXTRA_SEARCH_KEYWORD = "org.anvei.search.keyword";

    public static final String DATABASE_NAME = "anvei";

    public static final String TABLE_BOOK_SHELF_ITEM = "BookShelfItem";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
