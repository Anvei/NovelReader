package org.anvei.novelreader.novel;

import org.anvei.novelreader.model.NovelInfo;

import java.io.IOException;
import java.util.List;

public interface NovelWebsiteParser {

    List<NovelInfo> search(String keyWord) throws IOException;

    List<NovelInfo> searchByAuthor(String author);

    List<NovelInfo> searchByNovelName(String author);

}
