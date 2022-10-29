package org.anvei.novelreader.novel;

import org.anvei.novelreader.model.Novel;
import org.anvei.novelreader.model.NovelInfo;

import java.util.List;

/**
 * 网络小说加载器的抽象实现<br/><br/>
 *
 * 支持小说搜索功能：
 *      返回Novel对象列表，该列表中的小说都已经完成了对小说基本信息的加载 <br/><br/>
 *
 * 支持部分加载Novel对象，加载顺序如下:<br/>
 *      <li>小说基本信息</li>
 *      <li>小说目录</li>
 *      <li>小说章节内容</li>
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
