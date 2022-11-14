package org.anvei.novelreader.ui.view.novelpage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class AbstractNovelContentPage extends RelativeLayout implements INovelContentPage {

    public AbstractNovelContentPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 获取当前页面可以显示的文本最大行数
    public abstract void getMaxRowCount();

    // 获取当前页面每行最多可以显示的字数
    public abstract void getMaxCharCountPerRow();

    /**
     * 判断是否是章节的首页
     */
    public abstract boolean isFirstPage();

    /**
     * 设置章节名，如果给非首页的NovelContentPage设置，会抛出异常
     */
    public void setTitle() {
        if (isFirstPage()) {
            throw new IllegalStateException("Only NovelFirstPage can call this method");
        } else {
            throw new IllegalStateException("The NovelFirstPage need to override this method");
        }
    }

    public void setTitleFontSize(float size) {
        if (isFirstPage()) {
            throw new IllegalStateException("Only NovelFirstPage can call this method");
        } else {
            throw new IllegalStateException("The NovelFirstPage need to override this method");
        }
    }

    public void setTitleColor(float size) {
        if (isFirstPage()) {
            throw new IllegalStateException("Only NovelFirstPage can call this method");
        } else {
            throw new IllegalStateException("The NovelFirstPage need to override this method");
        }
    }

}
