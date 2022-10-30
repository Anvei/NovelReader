package org.anvei.novelreader.novel;

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
import java.util.Date;
import java.util.List;

public class SfacgParser implements NovelWebsiteParser {

    public SfacgParser() {
    }

    // The home page address of the SFACG website
    private static final String baseUrl = "https://book.sfacg.com/";

    //  prefix of the query API
    private static final String searchApiPrefix = "http://s.sfacg.com/?Key=";

    // suffix of the query API
    private static final String searchApiSuffix = "&S=1&SS=0";

    /**
     * Escape the keyword string into the hexadecimal string format required for the query URL
     */
    private String transferHexString(String key) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            //  Java does not allow dangerous characters in URLs, so '%' needs to be expressed as %25
            stringBuilder.append("%25u").append(Integer.toHexString(key.charAt(i)).toUpperCase());
        }
        return stringBuilder.toString();
    }

    /**
     * Android system requires that network requests need to be executed in child threads.
     */
    public List<NovelInfo> search(String keyWord) throws IOException {
        List<NovelInfo> novels = new ArrayList<>();
        String searchUrl = searchApiPrefix + transferHexString(keyWord) + searchApiSuffix;
        Document document = Jsoup.connect(searchUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
                .timeout(10000)
                .ignoreContentType(true)
                .get();
        Elements uls = document.select("#form1 > table.comic_cover.Height_px22.font_gray.space10px > tbody > tr > td > ul");
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
                NovelInfo novelInfo = new NovelInfo(novel)
                        .setPicUrl(imgSrc)
                        .setIntroduction(novelIntro)
                        .setUrl(novelUrl);
                novels.add(novelInfo);
            }
        }
        return novels;
    }

    @Override
    public List<NovelInfo> searchByAuthor(String author) {
        return null;
    }

    @Override
    public List<NovelInfo> searchByNovelName(String author) {
        return null;
    }

    public List<ChapterInfo> loadChapterList(String novelUrl) throws IOException {
        novelUrl = novelUrl + "/MainIndex/";
        List<ChapterInfo> chapterInfoList = new ArrayList<>();
        Document document = Jsoup.connect(novelUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
                .timeout(10000)
                .ignoreContentType(true)
                .get();
        Elements elements = document.select("body > div.container > div.wrap.s-list > div.story-catalog > div.catalog-list > ul > li > a");
        for (int i = 0; i < elements.size(); i++) {
            int index = i + 1;
            String chapterUrl = baseUrl.substring(0, baseUrl.length() - 1) + elements.get(i).attr("href");
            String title = elements.get(i).attr("title");
            ChapterInfo chapterInfo = new ChapterInfo(new Chapter(title), index);
            chapterInfo.addUrl(chapterUrl);
            chapterInfoList.add(chapterInfo);
        }
        return chapterInfoList;
    }

    public List<ChapterInfo> loadChapterList(int novelId) throws IOException {
        String novelUrl = baseUrl + "Novel/" + novelId + "/MainIndex/";
        return loadChapterList(novelUrl);
    }

    public Chapter loadChapter(ChapterInfo chapterInfo) throws IOException {
        int urlCount = chapterInfo.getUrlCount();
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < urlCount; i++) {
            String url = chapterInfo.getUrl(i);
            Document document = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .ignoreContentType(true)
                    .get();
            Elements paras = document.select("#ChapterBody > p");
            for (Element para : paras) {
                content.append("    ")
                        .append(para.text().trim())
                        .append("\n\n");
            }
        }
        chapterInfo.getChapter().setContent(content.toString());
        return chapterInfo.getChapter();
    }

}
