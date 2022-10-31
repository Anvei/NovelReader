package org.anvei.novelreader.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public static final String ANVEI_NOVEL_URL = "org.anvei.novel.url";

    public static final String ANVEI_NOVEL_NAME = "org.anvei.novel.name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
