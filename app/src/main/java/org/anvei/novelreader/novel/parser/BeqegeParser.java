package org.anvei.novelreader.novel.parser;

import org.anvei.novelreader.model.Chapter;
import org.anvei.novelreader.model.ChapterInfo;
import org.anvei.novelreader.model.Novel;
import org.anvei.novelreader.model.NovelInfo;
import org.anvei.novelreader.novel.WebsiteIdentifier;
import org.anvei.novelreader.novel.WebsiteNovelParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeqegeParser extends WebsiteNovelParser {

    private static final String homeUrl = "https://www.beqege.cc";

    private static final String searchApi = homeUrl + "/search.php";

    private static final String searchKey = "keyword";

    private static final String SELECT_NOVEL_LIST = "#main > div > div.panel-body > ul > li";

    private static final String SELECT_CHAPTER_LIST = "#list > dd > a";

    private static final String SELECT_CHAPTER_CONTENT = "#content > p";

    @Override
    public WebsiteIdentifier getWebsiteIdentifier() {
        return WebsiteIdentifier.UNKNOWN;
    }

    @Override
    public List<NovelInfo> search(String keyWord) {
        List<NovelInfo> novelInfoList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(searchApi)
                    .header(REQUEST_HEAD_KEY, REQUEST_HEAD_VALUE)
                    .timeout(getTimeOut())
                    .data(searchKey, keyWord)
                    .post();
            Elements divs = document.select(SELECT_NOVEL_LIST);
            for (Element div : divs) {
                String novelUrl = div.select("span.s2 > a").attr("href");
                String novelName = div.select("span.s2 > a").text();
                String author = div.select("span.s4").text();
                NovelInfo novelInfo = new NovelInfo(new Novel(novelName, author), getWebsiteIdentifier());
                novelInfo.setUrl(novelUrl);
                novelInfoList.add(novelInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getFilter() == null ? novelInfoList : getFilter().filter(novelInfoList);
    }

    @Override
    public List<NovelInfo> searchByAuthor(String author) {
        return search(author);
    }

    @Override
    public List<NovelInfo> searchByNovelName(String name) {
        return search(name);
    }

    @Override
    public List<ChapterInfo> loadNovel(NovelInfo novelInfo) {
        return loadNovel(novelInfo.getUrl());
    }

    @Override
    public List<ChapterInfo> loadNovel(String novelUrl) {
        List<ChapterInfo> chapterInfoList = new ArrayList<>();
        try {
            Document document = getDocument(novelUrl);
            Elements elements = document.select(SELECT_CHAPTER_LIST);
            for (Element element : elements) {
                String chapterUrl = homeUrl + element.attr("href");
                String chapterName = element.text();
                ChapterInfo chapterInfo = new ChapterInfo(new Chapter(chapterName), chapterInfoList.size()  + 1);
                chapterInfoList.add(chapterInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chapterInfoList;
    }

    @Override
    public Chapter loadChapter(ChapterInfo chapterInfo) {
        StringBuilder content = new StringBuilder();
        try {
            Document document = getDocument(chapterInfo.getUrl());
            Elements paras = document.select(SELECT_CHAPTER_CONTENT);
            for (int i = 1; i < paras.size(); i++) {
                content.append(PARA_PREFIX)
                        .append(paras.get(i).text().trim())
                        .append(PARA_SUFFIX);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Chapter chapter = chapterInfo.getChapter();
        chapter.setContent(content.toString());
        return chapter;
    }
}
