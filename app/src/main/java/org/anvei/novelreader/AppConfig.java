package org.anvei.novelreader;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 应用的全局配置信息
 */
public class AppConfig {

    private static final String READ_PAGE_CONFIG = "readPageConfig";
    private static final String READ_PAGE_CONFIG_CONTENT_FONT_SIZE = "contentFontSize";   // float

    // 小说默认字体大小16sp
    public static final float defaultNovelContentFontSize = 16;

    public static float getNovelContentFontSize() {
        SharedPreferences pref = App.getContext()
                .getSharedPreferences(READ_PAGE_CONFIG, Context.MODE_PRIVATE);
        return pref.getFloat(READ_PAGE_CONFIG_CONTENT_FONT_SIZE, defaultNovelContentFontSize);
    }

    // 更新字体配置信息
    public static void setNovelContentFontSize(float size) {
        new Thread(() -> {          // 涉及到内存的访问，在子线程中执行
            SharedPreferences pref = App.getContext()
                    .getSharedPreferences(READ_PAGE_CONFIG, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putFloat(READ_PAGE_CONFIG_CONTENT_FONT_SIZE, size);
            editor.apply();
        }).start();
    }

}
