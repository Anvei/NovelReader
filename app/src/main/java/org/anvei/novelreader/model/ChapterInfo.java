package org.anvei.novelreader.model;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChapterInfo {

    private final Chapter chapter;

    private File cache;

    private final int index;

    private final List<String> urls = new ArrayList<>();         // url地址，可能会有多页

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

    public void addUrl(String url) {
        urls.add(url);
    }

    public String getUrl(@IntRange(from = 0) int index) {
        return urls.get(index);
    }

    public int getUrlCount() {
        return urls.size();
    }
}
