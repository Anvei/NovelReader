package org.anvei.novelreader.ui.view.novelpage;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.IntRange;

public interface INovelContentPage {

    // 设置当前页面的显示内容
    void setContent();

    void setContentFontSize(float size);

    void setContentColor(Color color);

    // 设置页眉文本
    void setHeaderText(String text);

    void setHeaderTextFontSize(float size);

    void setHeaderTextFontColor(Color color);

    // 设置页脚的文本
    void setFooterText(String text);

    void setFooterTextFontSize(float size);

    void setFooterTextColor(Color color);

    interface OnClickRegionListener {
        /**
         * 监听点击事件
         * @param xPercent 点击事件位置在NovelContentPage中x方向的百分比
         * @param yPercent 点击事件位置在NovelContentPage中y方向的百分比
         */
        void onClickRegion(@IntRange(from = 0, to = 100)int xPercent,
                @IntRange(from = 0, to = 100)int yPercent);
    }

    // 设置NovelContentPage的点击事件区域监听器
    void setOnClickRegionListener(OnClickRegionListener listener);

    void setBackgroundColor(Color color);

    void setBackground(Bitmap bitmap);
}
