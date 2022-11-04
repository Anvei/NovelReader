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

public class W147xsParser extends WebsiteNovelParser {

    private static final String homeUrl = "https://www.147xs.org";

    private static final String searchApi = "https://www.147xs.org/search.php";

    private static final String SELECT_NOVEL_LIST = "#bookcase_list > tr";

    private static final String SELECT_CHAPTER_LIST = "#list > dl > dd > a";

    private static final String SELECT_CHAPTER_CONTENT = "#content > p";

    public W147xsParser() {
    }

    @Override
    public WebsiteIdentifier getWebsiteIdentifier() {
        return WebsiteIdentifier.W147XS;
    }

    @Override
    public List<NovelInfo> search(String keyWord) {
        List<NovelInfo> novelInfoList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(searchApi)
                    .header(REQUEST_HEAD_KEY, REQUEST_HEAD_VALUE)
                    .timeout(getTimeOut())
                    .data("keyword", keyWord)
                    .post();
            Elements elements = document.select(SELECT_NOVEL_LIST);
            for (Element element : elements) {
                String novelUrl = element.select("td:nth-child(2) > a").attr("href");
                String novelName = element.select("td:nth-child(2) > a").text();
                String author = element.select("td:nth-child(4)").text();
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
                String chapterName = element.text();
                String chapterUrl = homeUrl + element.attr("href");
                ChapterInfo chapterInfo = new ChapterInfo(new Chapter(chapterName), chapterInfoList.size() + 1);
                chapterInfo.setUrl(chapterUrl);
                chapterInfoList.add(chapterInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chapterInfoList;
    }

    @Override
    public Chapter loadChapter(ChapterInfo chapterInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Document document = getDocument(chapterInfo.getUrl());
            Elements paras = document.select(SELECT_CHAPTER_CONTENT);      // 获取<p></p>标签
            for (Element para : paras) {
                String paraText = para.text().trim();
                if (!paraText.startsWith("【")) {      // 过滤网站的垃圾信息
                    stringBuilder.append(PARA_PREFIX)
                            .append(para.text().trim())
                            .append(PARA_SUFFIX);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Chapter chapter = chapterInfo.getChapter();
        chapter.setContent(stringBuilder.toString());
        return chapter;
    }

}
