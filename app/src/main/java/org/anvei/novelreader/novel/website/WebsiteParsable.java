package org.anvei.novelreader.novel.website;


import org.anvei.novelreader.beans.WebsiteChapter;
import org.anvei.novelreader.beans.WebsiteNovel;

import java.util.List;

public interface WebsiteParsable {

    WebsiteIdentifier getWebsiteIdentifier();

    List<WebsiteNovel> search(String keyWord);

    List<WebsiteNovel> searchByAuthor(String author);

    List<WebsiteNovel> searchByNovelName(String name);

    List<WebsiteChapter> loadNovel(WebsiteNovel novelInfo);

    List<WebsiteChapter> loadNovel(String novelUrl);

    WebsiteChapter loadChapter(WebsiteChapter chapterInfo);

}
