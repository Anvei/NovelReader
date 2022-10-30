package org.anvei.novelreader.novel;

import org.anvei.novelreader.model.Novel;
import org.anvei.novelreader.model.NovelInfo;

import java.util.List;

/**
 * Abstract implementation of the web novel loader<br/><br/>
 */
public abstract class InternetNovelLoader implements NovelLoader {

    protected String key;

    protected InternetNovelLoader(String key) {
        this.key = key;
    }

//    public abstract List<NovelInfo> search(String keyWord);
//
//    /**
//     * Search according to the given novel name
//     */
//    public abstract List<NovelInfo> searchByNovelName(String novelName);
//
//    /**
//     * Search according to the given author name
//     */
//    public abstract List<NovelInfo> searchByAuthor(String author);
//
//    /**
//     * 加载小说基本信息
//     */
//    public abstract void loadNovelInfo();
//
//    /**
//     * 加载小说目录
//     */
//    public abstract void loadNovelContent();
//
//    /**
//     * 章节内容加载
//     */
//    public abstract void loadChapterContent();
}
