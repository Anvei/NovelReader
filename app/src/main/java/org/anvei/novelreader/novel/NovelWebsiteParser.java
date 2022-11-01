package org.anvei.novelreader.novel;

import androidx.annotation.IntRange;

public abstract class NovelWebsiteParser implements NovelWebsiteParsable {

    public static String REQUEST_HEAD_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36";

    public static String REQUEST_HEAD_KEY = "User-Agent";

    private int timeOut = 10000;

    private NovelSearchFilter filter;

    protected NovelWebsiteParser() {
    }

    protected NovelWebsiteParser(NovelSearchFilter filter) {
        this.filter = filter;
    }

    public NovelWebsiteParser setTimeOut(@IntRange(from = 200, to = 30000) int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public NovelWebsiteParser setFilter(NovelSearchFilter filter) {
        this.filter = filter;
        return this;
    }

    public NovelSearchFilter getFilter() {
        return filter;
    }
}
