package org.anvei.novelreader.novel.parser;

import androidx.annotation.NonNull;

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

public class BiqumuParser extends WebsiteNovelParser {

    private static final String homeUrl = "http://www.biqumu.com";

    private static final String searchApi = "http://www.biqumu.com/search.html";

    private static final String searchKey = "s";

    private static final String SELECT_NOVEL_LIST = "body > div.container > div:nth-child(1) > div > ul > li";

    private static final String SELECT_CHAPTER_LIST = "body > div.container > div:nth-child(2) > div > ul";

    private static final String SELECT_CHAPTER_CONTENT = "body > div.container > div.row.row-detail > div > div > p";

    private static final String SELECT_NEXT_CHAPTER = "body > div.container > div.row.row-detail div.read_btn > a:nth-child(4)";

    private static final String SELECT_OPTION = "body > div.container > div:nth-child(2) > div > div:nth-child(4) > select > option";

    public BiqumuParser() {
    }

    @Override
    public WebsiteIdentifier getWebsiteIdentifier() {
        return WebsiteIdentifier.BIQUMU;
    }

    private Document getSearchResult(@NonNull String keyWord) throws IOException {
        return Jsoup.connect(searchApi)
                .header(REQUEST_HEAD_KEY, REQUEST_HEAD_VALUE)
                .data(searchKey, keyWord)
                .timeout(getTimeOut())
                .post();
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
                NovelInfo novelInfo = new NovelInfo(new Novel(name, author), getWebsiteIdentifier());
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
            // 现加载首页
            Document document = getDocument(novelUrl);
            Elements lis = document.select(SELECT_CHAPTER_LIST + ":nth-child(4) > li");
            int index = 1;
            for (Element li : lis) {
                String url = homeUrl + li.select("a").attr("href");
                String title = li.select("a").text();
                ChapterInfo chapterInfo = new ChapterInfo(new Chapter(title), index);
                chapterInfo.setUrl(url);
                chapterInfoList.add(chapterInfo);
                index++;
            }
            List<Document> others = new ArrayList<>();
            Document otherPage = getDocument(novelUrl + "1/");
            Elements options = otherPage.select(SELECT_OPTION);      // 处理多页目录
            if (options.size() != 0) {
                others.add(otherPage);
                for (int i = 2; i < options.size(); i++) {
                    others.add(getDocument(homeUrl + options.get(i).attr("value")));
                }
            }
            for (Document doc : others) {
                // 处理网站的反爬虫设计障碍
                String style = doc.select("head style").toString();
                int start = 0;
                int last = 0;
                String[] splits = style.split("[()]");
                for (int i = 1; i < splits.length; i = i + 2) {
                    int temp = Integer.parseInt(splits[i]);
                    if (temp > last) {
                        start++;
                        last = temp;
                    } else {
                        break;
                    }
                }
                int end = (splits.length - 1) / 2 - start;
                lis = doc.select(SELECT_CHAPTER_LIST + " > li");
                for (int i = start; i < lis.size() - end; i++) {
                    String url = homeUrl + lis.get(i).select("a").attr("href");
                    String title = lis.get(i).select("a").text();
                    ChapterInfo chapterInfo = new ChapterInfo(new Chapter(title), index);
                    chapterInfo.setUrl(url);
                    chapterInfoList.add(chapterInfo);
                    index++;
                }
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
            String nextUrl = chapterInfo.getUrl();
            boolean hasNext = true;         // 是否有下一页
            boolean isFirst = true;
            while (hasNext) {
                Document document = getDocument(nextUrl);
                nextUrl = homeUrl + document.select(SELECT_NEXT_CHAPTER).attr("href");
                hasNext = nextUrl.charAt(nextUrl.length() - 7) == '_';
                Elements paras = document.select(SELECT_CHAPTER_CONTENT);
                // 去掉第二页之后的重复行
                int i;
                if (isFirst) {
                    i = 0;
                    isFirst = false;
                } else {
                    i = 1;
                }
                while (i < paras.size()) {
                    content.append(PARA_PREFIX)
                            .append(paras.get(i).text().trim())
                            .append(PARA_SUFFIX);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        chapterInfo.getChapter().setContent(content.toString());
        return chapterInfo.getChapter();
    }

}
