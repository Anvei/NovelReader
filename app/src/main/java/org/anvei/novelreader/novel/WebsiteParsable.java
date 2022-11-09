package org.anvei.novelreader.novel;


import org.anvei.novelreader.beans.Chapter;
import org.anvei.novelreader.beans.WebsiteChapterInfo;
import org.anvei.novelreader.beans.WebsiteNovelInfo;

import java.util.List;

public interface WebsiteParsable {

    WebsiteIdentifier getWebsiteIdentifier();

    List<WebsiteNovelInfo> search(String keyWord);

    List<WebsiteNovelInfo> searchByAuthor(String author);

    List<WebsiteNovelInfo> searchByNovelName(String name);

    List<WebsiteChapterInfo> loadNovel(WebsiteNovelInfo novelInfo);

    List<WebsiteChapterInfo> loadNovel(String novelUrl);

    Chapter loadChapter(WebsiteChapterInfo chapterInfo);

}
