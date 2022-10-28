package org.anvei.novelreader.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

// 表示一个章节
public class Chapter {

    private final String name;              // 章节名

    private final String content;                 // 章节内容

    private Date updateTime;                // 更新时间

    private int wordCount = 0;

    public Chapter(@NonNull String name, @NonNull String content) {
        this.name = name;
        this.content = content;
        wordCount = content.length();
    }

    public @NonNull String getName() {
        return name;
    }

    public @NonNull String getContent() {
        return content;
    }

    public @Nullable Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getCharCount() {
        return wordCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "Chapter{" +
                "name='" + name + '\'' +
                ", updateTime=" + updateTime +
                ", wordCount=" + wordCount +
                '}';
    }
}
