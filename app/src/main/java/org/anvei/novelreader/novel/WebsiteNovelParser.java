package org.anvei.novelreader.novel;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public abstract class WebsiteNovelParser implements WebsiteParsable {

    public static String REQUEST_HEAD_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36";

    public static String REQUEST_HEAD_KEY = "User-Agent";

    protected static String PARA_PREFIX = "        ";       // 段落的前缀：八个空格字符，保持缩进两个中文字符

    protected static String PARA_SUFFIX = "\n";           // 段落以两个换行符结尾

    private int timeOut = 10000;

    private WebsiteNovelFilter filter;

    protected WebsiteNovelParser() {
    }

    protected WebsiteNovelParser(WebsiteNovelFilter filter) {
        this.filter = filter;
    }

    public WebsiteNovelParser setTimeOut(@IntRange(from = 200, to = 30000) int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public WebsiteNovelParser setFilter(WebsiteNovelFilter filter) {
        this.filter = filter;
        return this;
    }

    public WebsiteNovelFilter getFilter() {
        return filter;
    }

    /**
     * get a Document object for the url
     */
    protected Document getDocument(@NonNull String url) throws IOException {
        return Jsoup.connect(url)
                .header(REQUEST_HEAD_KEY, REQUEST_HEAD_VALUE)
                .timeout(getTimeOut())
                .get();
    }
}
