package org.anvei.novelreader.novel.parser;

import org.anvei.novelreader.model.Chapter;
import org.anvei.novelreader.model.ChapterInfo;
import org.anvei.novelreader.model.NovelInfo;
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
    public List<NovelInfo> search(String keyWord) {
        return null;
    }

    @Override
    public List<NovelInfo> searchByAuthor(String author) {
        return null;
    }

    @Override
    public List<NovelInfo> searchByNovelName(String name) {
        return null;
    }

    @Override
    public List<ChapterInfo> loadNovel(NovelInfo novelInfo) {
        return null;
    }

    @Override
    public List<ChapterInfo> loadNovel(String novelUrl) {
        return null;
    }

    @Override
    public Chapter loadChapter(ChapterInfo chapterInfo) {
        return null;
    }
}
