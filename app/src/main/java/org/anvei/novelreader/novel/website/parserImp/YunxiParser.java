package org.anvei.novelreader.novel.website.parserImp;

import org.anvei.novelreader.beans.WebsiteChapter;
import org.anvei.novelreader.beans.WebsiteNovel;
import org.anvei.novelreader.novel.website.WebsiteIdentifier;
import org.anvei.novelreader.novel.website.WebsiteNovelParser;

import java.util.List;

public class YunxiParser extends WebsiteNovelParser {

    private static final String homeUrl = "https://www.yunxibook.com";

    private static final String searchApi = "https://www.yunxibook.com/modules/article/search.php";

    @Override
    public WebsiteIdentifier getWebsiteIdentifier() {
        return null;
    }

    @Override
    public List<WebsiteNovel> search(String keyWord) {
        return null;
    }

    @Override
    public List<WebsiteNovel> searchByAuthor(String author) {
        return null;
    }

    @Override
    public List<WebsiteNovel> searchByNovelName(String name) {
        return null;
    }

    @Override
    public List<WebsiteChapter> loadNovel(WebsiteNovel novelInfo) {
        return null;
    }

    @Override
    public List<WebsiteChapter> loadNovel(String novelUrl) {
        return null;
    }

    @Override
    public WebsiteChapter loadChapter(WebsiteChapter chapterInfo) {
        return null;
    }
}
