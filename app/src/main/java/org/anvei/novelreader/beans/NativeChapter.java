package org.anvei.novelreader.beans;

import androidx.annotation.IntRange;

public class NativeChapter {

    private String chapterName;

    private String chapterContent;

    @IntRange(from = 1)
    private int index;                  // 章节的序号、从1开始

    private int startPosition;          // 章节在本地文件的起始位置
    private int endPosition;            // 章节在本地文件的结束位置

    public NativeChapter(String chapterName, String chapterContent, int index) {
        this.chapterName = chapterName;
        this.chapterContent = chapterContent;
        this.index = index;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }
}
