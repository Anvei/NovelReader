package org.anvei.novelreader.model;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.Serializable;

public class ChapterInfo implements Serializable {

    private final Chapter chapter;

    private File cache;

    private final int index;

    private String url;         // url地址

    public ChapterInfo(@NonNull Chapter chapter, int index) {
        this.chapter = chapter;
        this.index = index;
    }

    public ChapterInfo setCache(File cache) {
        this.cache = cache;
        return this;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public File getCache() {
        return cache;
    }

    public int getIndex() {
        return index;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


}
