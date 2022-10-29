package org.anvei.novelreader.novel;

import org.anvei.novelreader.model.Novel;
import org.anvei.novelreader.model.NovelInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SfacgNovelLoader{

    public SfacgNovelLoader() {
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
    private static String transferHexString(String key) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            //  Java does not allow dangerous characters in URLs, so '%' needs to be expressed as %25
            stringBuilder.append("%25u").append(Integer.toHexString(key.charAt(i)).toUpperCase());
        }
        return stringBuilder.toString();
    }

    public static List<NovelInfo> search(String keyWord) throws IOException {
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
                String novelIntro = lis.get(1).text();
                NovelInfo novelInfo = new NovelInfo(new Novel(novelName, ""))
                        .setPicUrl(imgSrc)
                        .setIntroduction(novelIntro)
                        .setUrl(novelUrl);
                novels.add(novelInfo);
            }
        }
        return novels;
    }
}
