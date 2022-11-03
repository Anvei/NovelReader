package org.anvei.novelreader.novel;

import org.anvei.novelreader.novel.parser.BiqumuParser;
import org.anvei.novelreader.novel.parser.SfacgParser;
import org.anvei.novelreader.novel.parser.W147xsParser;

import java.util.ArrayList;
import java.util.List;

public class NovelParserFactory {

    public static List<WebsiteNovelParser> getWebsiteNovelParsers() {
        List<WebsiteNovelParser> parsers = new ArrayList<>();
        parsers.add(new W147xsParser());
        parsers.add(new BiqumuParser());
        parsers.add(new SfacgParser());
        return parsers;
    }

}
