package org.anvei.novelreader.ui.view.novelpage;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.anvei.novelreader.R;

public class NovelContentPage extends RelativeLayout {

    private View root;
    private TextView chapterContent;
    private TextView headerText;
    private TextView footerText;

    private String contentStr;
    private int firstCharIndex;             // 第一个字符在contentStr中的位置
    private int maxLines;                   // chapterContent显示的最大行数
    private int maxCharsPerLine;            // chapterContent单行显示的最大字数
    private float contentFontSize;

    public NovelContentPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        root = LayoutInflater.from(getContext()).inflate(R.layout.view_novel_content_page, this);
        chapterContent = root.findViewById(R.id.ncp_content);
        headerText = root.findViewById(R.id.rp_header_chapter_name);
        footerText = root.findViewById(R.id.ncp_footer_novel);
    }

    public void setHeaderText(CharSequence text) {
        headerText.setText(text);
    }

    public void setFooterText(CharSequence text) {
        footerText.setText(text);
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    public int getMaxCharsPerLine() {
        return maxCharsPerLine;
    }

    public void setMaxCharsPerLine(int maxCharsPerLine) {
        this.maxCharsPerLine = maxCharsPerLine;
    }

    /**
     * 负责计算maxLines、maxCharsPerLine
     */
    protected void computeContentInfo() {

    }

    public float getContentFontSize() {
        return contentFontSize;
    }

    // 设置内容字体大小时，会重新计算chapterContent的参数
    public void setContentFontSize(float contentFontSize) {
        this.contentFontSize = contentFontSize;
        computeContentInfo();
        invalidate();
    }

    protected String getContent() {
        for (int i = firstCharIndex; i < maxLines * maxCharsPerLine; i++) {

        }
        return "";
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        chapterContent.setTextSize(contentFontSize);
        chapterContent.setText(getContent());
        super.dispatchDraw(canvas);
    }
}
