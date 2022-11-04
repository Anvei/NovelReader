package org.anvei.novelreader.model;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Novel implements Serializable {

    private final String name;                  // 小说名称

    private final String author;                // 小说作者

    private final List<Chapter> chapterList = new ArrayList<>();        // 章节列表

    private int chapterCount = 0;       // 章节数

    private Status novelStatus = Status.UNKNOWN;         // 小说连载状态，默认为未知状态

    private int wordCount = 0;         // 小说字数

    private Date lastUpdate;

    public Novel(@NonNull String name, @NonNull String author) {
        this.name = name;
        this.author = author;
    }

    // 表示小说连载状态
    public enum Status {
        UPDATING,           // 连载中
        FINISHED,           // 完结
        UNKNOWN             // 未知
    }

    public @NonNull String getName() {
        return name;
    }

    public @NonNull String getAuthor() {
        return author;
    }

    public void addChapter(@NonNull Chapter chapter) {
        chapterList.add(chapter);
        ++chapterCount;
        wordCount += chapter.getCharCount();
    }

    public @Nullable Chapter getLastChapter() {
        return chapterList.size() == 0 ? null : chapterList.get(chapterList.size() - 1);
    }

    // 根据章节号获取章节，从1开始
    public Chapter getChapter(@IntRange(from = 1) int index) {
        return chapterList.get(index - 1);
    }

    public @NonNull Status getNovelStatus() {
        return novelStatus;
    }

    public void setNovelStatus(@NonNull Status novelStatus) {
        this.novelStatus = novelStatus;
    }

    public int getWordCount() {
        return wordCount;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Novel{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
