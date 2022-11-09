package org.anvei.novelreader.novel.parser;

import org.anvei.novelreader.beans.Chapter;
import org.anvei.novelreader.beans.WebsiteChapterInfo;
import org.anvei.novelreader.beans.WebsiteNovelInfo;
import org.anvei.novelreader.novel.WebsiteIdentifier;
import org.anvei.novelreader.novel.WebsiteNovelParser;

import java.util.List;

public class YunxiParser extends WebsiteNovelParser {

    private static final String homeUrl = "https://www.yunxibook.com";

    private static final String searchApi = "https://www.yunxibook.com/modules/article/search.php";

    @Override
    public WebsiteIdentifier getWebsiteIdentifier() {
        return null;
    }

    @Override
    public List<WebsiteNovelInfo> search(String keyWord) {
        return null;
    }

    @Override
    public List<WebsiteNovelInfo> searchByAuthor(String author) {
        return null;
    }

    @Override
    public List<WebsiteNovelInfo> searchByNovelName(String name) {
        return null;
    }

    @Override
    public List<WebsiteChapterInfo> loadNovel(WebsiteNovelInfo novelInfo) {
        return null;
    }

    @Override
    public List<WebsiteChapterInfo> loadNovel(String novelUrl) {
        return null;
    }

    @Override
    public Chapter loadChapter(WebsiteChapterInfo chapterInfo) {
        return null;
    }
}
