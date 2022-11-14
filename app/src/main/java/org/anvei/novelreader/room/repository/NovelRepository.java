package org.anvei.novelreader.room.repository;

import org.anvei.novelreader.annotations.SubAnnotation;
import org.anvei.novelreader.beans.WebsiteNovel;
import org.anvei.novelreader.room.AppDatabase;
import org.anvei.novelreader.room.dao.WebsiteNovelDao;

import java.util.List;

@SubAnnotation
public final class NovelRepository {

    public static List<WebsiteNovel> getAllBookShelfNovel() {
        return AppDatabase.getInstance().getWebsiteNovelDao().queryAllNovel();
    }

    /**
     * 根据WebsiteNovel的uid进行更新信息
     */
    public static void updateBookShelfNovel(WebsiteNovel novel) {
        AppDatabase.getInstance().getWebsiteNovelDao().updateNovel(novel);
    }

    public static void deleteNovelFromBookShelf(WebsiteNovel novel) {
        AppDatabase.getInstance().getWebsiteNovelDao().deleteNovel(novel);
    }

    // 只用来添加新数据
    public static boolean addNovelToBookShelf(WebsiteNovel novel) {
        WebsiteNovelDao websiteNovelDao = AppDatabase.getInstance().getWebsiteNovelDao();
        if (websiteNovelDao.queryByAuthorAndNovelName(novel.author, novel.novelName) == null) {
            websiteNovelDao.addNovel(novel);
            return true;
        }
        return false;
    }

}
