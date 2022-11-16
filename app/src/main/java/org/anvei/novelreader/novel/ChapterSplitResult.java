package org.anvei.novelreader.novel;

import androidx.annotation.NonNull;

public class ChapterSplitResult {

    private final String chapterName;             // 章节名称

    private final int index;                      // 章节序号
    private final long startIndex;                 // 章节内容起始下标（章节内容不包括章节名）
    private final long endIndex;                   // 章节内容结束下标

    public ChapterSplitResult(String chapterName, int index, long startIndex, long endIndex) {
        this.chapterName = chapterName;
        this.index = index;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getChapterName() {
        return chapterName;
    }

    public int getIndex() {
        return index;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public long getEndIndex() {
        return endIndex;
    }

    @NonNull
    @Override
    public String toString() {
        return "ChapterSplitResult{" +
                "chapterName='" + chapterName + '\'' +
                ", index=" + index +
                ", startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                '}';
    }
}
