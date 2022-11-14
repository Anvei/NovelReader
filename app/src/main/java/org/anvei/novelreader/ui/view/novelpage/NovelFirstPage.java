package org.anvei.novelreader.ui.view.novelpage;

import android.content.Context;
import android.util.AttributeSet;

public class NovelFirstPage extends NovelContentPage {

    public NovelFirstPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFirstPage() {
        return true;
    }

    @Override
    public void setTitle() throws IllegalStateException {
        super.setTitle();
    }

    @Override
    public void setTitleFontSize(float size) throws IllegalStateException {
        super.setTitleFontSize(size);
    }

    @Override
    public void setTitleColor(float size) throws IllegalStateException {
        super.setTitleColor(size);
    }

}
