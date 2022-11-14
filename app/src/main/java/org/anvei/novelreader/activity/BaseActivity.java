package org.anvei.novelreader.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected static final String EXTRA_NOVEL_INFO = "org.anvei.novel.info";

    protected static final String EXTRA_NOVEL_POSITION = "org.anvei.novel.position";

    protected static final String EXTRA_SEARCH_PARSER = "org.anvei.parser";

    protected static final String EXTRA_SEARCH_KEYWORD = "org.anvei.search.keyword";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
