package org.anvei.novelreader.novel;

public abstract class NovelWebsiteParser implements NovelWebsiteParsable {

    public static String REQUEST_HEAD_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36";

    public static String REQUEST_HEAD_KEY = "User-Agent";

    protected int timeOut = 10000;

    protected NovelSearchFilter filter;

    protected NovelWebsiteParser() {
    }

    protected NovelWebsiteParser(NovelSearchFilter filter) {
        this.filter = filter;
    }

    public NovelWebsiteParser setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public NovelWebsiteParser setFilter(NovelSearchFilter filter) {
        this.filter = filter;
        return this;
    }
}
