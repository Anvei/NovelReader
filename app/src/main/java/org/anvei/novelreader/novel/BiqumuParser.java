package org.anvei.novelreader.novel;

import androidx.annotation.NonNull;

import org.anvei.novelreader.model.Chapter;
import org.anvei.novelreader.model.ChapterInfo;
import org.anvei.novelreader.model.Novel;
import org.anvei.novelreader.model.NovelInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BiqumuParser extends NovelWebsiteParser {

    private static final String homeUrl = "http://www.biqumu.com";

    private static final String searchApi = "http://www.biqumu.com/search.html";

    private static final String searchKey = "s";

    private static final String SELECT_NOVEL_LIST = "body > div.container > div:nth-child(1) > div > ul > li";

    private static final String SELECT_CHAPTER_LIST = "body > div.container > div:nth-child(2) > div > ul:nth-child(4) > li";

    public static final String SELECT_CHAPTER_CONTENT = "body > div.container > div.row.row-detail > div > div > p";

    public static final String SELECT_NEXT_CHAPTER = "body > div.container > div.row.row-detail div.read_btn > a:nth-child(4)";

    public BiqumuParser() {
    }

    private Document getSearchResult(@NonNull String keyWord) throws IOException {
        return Jsoup.connect(searchApi)
                .data(searchKey, keyWord)
                .timeout(getTimeOut())
                .post();
    }

    private Document getDocument(@NonNull String url) throws IOException {
        return Jsoup.connect(url)
                .timeout(getTimeOut())
                .get();
    }
    /**
     * 只解析获得了小说名称、作者、链接
     */
    @Override
    public List<NovelInfo> search(String keyWord) {
        List<NovelInfo> novelInfoList = new ArrayList<>();
        try {
            Document document = getSearchResult(keyWord);
            Elements lis = document.select(SELECT_NOVEL_LIST);
            for (Element li : lis) {
                String novelUrl = li.select("span.n2 > a").attr("href");
                String name = li.select("span.n2 > a").text();
                String author = li.select("span.n4").text();
                NovelInfo novelInfo = new NovelInfo(new Novel(name, author));
                novelInfo.setUrl(homeUrl + novelUrl);
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
            Elements lis = document.select(SELECT_CHAPTER_LIST);
            int index = 1;
            for (Element li : lis) {
                String url = homeUrl + li.select("a").attr("href");
                String title = li.select("a").text();
                ChapterInfo chapterInfo = new ChapterInfo(new Chapter(title), index);
                chapterInfo.addUrl(url);
                chapterInfoList.add(chapterInfo);
                index++;
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
            String nextUrl = chapterInfo.getUrl(0);
            boolean hasNext = true;
            while (hasNext) {
                Document document = getDocument(nextUrl);
                nextUrl = homeUrl + document.select(SELECT_NEXT_CHAPTER).attr("href");
                hasNext = nextUrl.charAt(nextUrl.length() - 7) == '_';
                Elements paras = document.select(SELECT_CHAPTER_CONTENT);
                for (Element para : paras) {
                    content.append("    ")
                            .append(para.text().trim())
                            .append("\n\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        chapterInfo.getChapter().setContent(content.toString());
        return chapterInfo.getChapter();
    }

}
