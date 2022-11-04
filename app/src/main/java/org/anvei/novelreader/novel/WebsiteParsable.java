package org.anvei.novelreader.novel;

import org.anvei.novelreader.model.Chapter;
import org.anvei.novelreader.model.ChapterInfo;
import org.anvei.novelreader.model.NovelInfo;

import java.util.List;

public interface WebsiteParsable {

    WebsiteIdentifier getWebsiteIdentifier();

    List<NovelInfo> search(String keyWord);

    List<NovelInfo> searchByAuthor(String author);

    List<NovelInfo> searchByNovelName(String name);

    List<ChapterInfo> loadNovel(NovelInfo novelInfo);

    List<ChapterInfo> loadNovel(String novelUrl);

    Chapter loadChapter(ChapterInfo chapterInfo);

}
