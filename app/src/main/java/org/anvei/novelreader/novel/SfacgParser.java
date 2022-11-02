package org.anvei.novelreader.novel;

import androidx.annotation.NonNull;

import org.anvei.novelreader.model.Chapter;
import org.anvei.novelreader.model.ChapterInfo;
import org.anvei.novelreader.model.Novel;
import org.anvei.novelreader.model.NovelInfo;
import org.anvei.novelreader.model.WebsiteIdentifier;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SfacgParser extends NovelWebsiteParser {

    public static final WebsiteIdentifier identifier = WebsiteIdentifier.SFACG;

    // The home page address of the SFACG website
    private static final String homeUrl = "https://book.sfacg.com/";

    //  prefix of the query API
    private static final String searchApiPrefix = "http://s.sfacg.com/?Key=";

    // suffix of the query API
    private static final String searchApiSuffix = "&S=1&SS=0";

    // css selector string for getting novel list
    private static final String SELECT_NOVEL_LIST = "#form1 > table.comic_cover.Height_px22.font_gray.space10px > tbody > tr > td > ul";

    // css selector string for getting chapter list
    private static final String SELECT_CHAPTER_LIST = "body > div.container > div.wrap.s-list > div.story-catalog > div.catalog-list > ul > li > a";

    //  css selector string for getting chapter content
    private static final String SELECT_CHAPTER_CONTENT = "#ChapterBody > p";

    public SfacgParser() {
    }

    /**
     * Escape the keyword string into the hexadecimal string format required for the query URL
     */
    private String transferHexString(@NonNull String key) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            //  Java does not allow dangerous characters in URLs, so '%' needs to be expressed as %25
            stringBuilder.append("%25u").append(Integer.toHexString(key.charAt(i)).toUpperCase());
        }
        return stringBuilder.toString();
    }

    /**
     * get a Document object for the url
     */
    private Document getDocument(@NonNull String url) throws IOException {
        return Jsoup.connect(url)
                .header(REQUEST_HEAD_KEY, REQUEST_HEAD_VALUE)
                .timeout(getTimeOut())
                .ignoreContentType(true)
                .get();
    }
    /**
     * Android system requires that network requests need to be executed in child threads.
     */
    @Override
    public List<NovelInfo> search(String keyWord) {
        List<NovelInfo> novels = new ArrayList<>();
        String searchUrl = searchApiPrefix + transferHexString(keyWord) + searchApiSuffix;
        try {
            Document document = getDocument(searchUrl);
            Elements uls = document.select(SELECT_NOVEL_LIST);
            for (Element ul : uls) {
                Elements lis = ul.getElementsByTag("li");
                if (lis.size() == 2) {
                    String imgSrc = "https:" + lis.get(0).select("img").attr("src");
                    String novelName = lis.get(0).select("img").attr("alt");
                    String novelUrl = lis.get(1).select("strong > a").attr("href");
                    String tempIntro = lis.get(1).text();
                    String[] splits = tempIntro.split(" ", 5);
                    String author;
                    String novelIntro;
                    Date lastUpdate = null;
                    if (splits.length == 5) {
                        novelIntro = splits[4];
                        String[] splits2 = splits[2].split("/");
                        if (splits2.length == 4) {
                            author = splits2[0];
                            lastUpdate = new Date(Integer.parseInt(splits2[1]),
                                    Integer.parseInt(splits2[1]),
                                    Integer.parseInt(splits2[1]));
                        } else {
                            author = "Unknown";
                        }
                    } else {
                        novelIntro = tempIntro;
                        author = "Unknown";
                    }
                    Novel novel = new Novel(novelName, author);
                    novel.setLastUpdate(lastUpdate);
                    NovelInfo novelInfo = new NovelInfo(novel, identifier)
                            .setPicUrl(imgSrc)
                            .setIntroduction(novelIntro)
                            .setUrl(novelUrl);
                    novels.add(novelInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getFilter() == null ? novels : getFilter().filter(novels);
    }

    @Override
    public List<NovelInfo> searchByAuthor(String author) {
        return search(author);
    }

    @Override
    public List<NovelInfo> searchByNovelName(String author) {
        return search(author);
    }

    @Override
    public List<ChapterInfo> loadNovel(@NonNull NovelInfo novelInfo) {
        return loadNovel(novelInfo.getUrl());
    }

    @Override
    public List<ChapterInfo> loadNovel(String novelUrl) {
        novelUrl = novelUrl + "/MainIndex/";
        List<ChapterInfo> chapterInfoList = new ArrayList<>();
        try {
            Document document = getDocument(novelUrl);
            Elements elements = document.select(SELECT_CHAPTER_LIST);
            for (int i = 0; i < elements.size(); i++) {
                int index = i + 1;
                String chapterUrl = homeUrl.substring(0, homeUrl.length() - 1) + elements.get(i).attr("href");
                String title = elements.get(i).attr("title");
                ChapterInfo chapterInfo = new ChapterInfo(new Chapter(title), index);
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
        StringBuilder content = new StringBuilder();
        try {
            Document document = getDocument(chapterInfo.getUrl());
            Elements paras = document.select(SELECT_CHAPTER_CONTENT);
            for (Element para : paras) {
                content.append("    ")
                        .append(para.text().trim())
                        .append("\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        chapterInfo.getChapter().setContent(content.toString());
        return chapterInfo.getChapter();
    }

}
