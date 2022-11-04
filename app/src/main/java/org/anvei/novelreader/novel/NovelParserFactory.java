package org.anvei.novelreader.novel;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

// 为了降低耦合度，采用了反射机制进行加载NovelParser
public final class NovelParserFactory {

    private static final String defaultLoadPath = "org.anvei.novelreader.novel.parser";

    private static String getParserForFieldName(String fieldName) {
        return fieldName.charAt(0) +
                fieldName.substring(1).toLowerCase() +
                "Parser";
    }

    private static @Nullable WebsiteNovelParser getParser(String identifierName) {
        WebsiteNovelParser res = null;
        String parserName = getParserForFieldName(identifierName);
        try {
            res = (WebsiteNovelParser) Class.forName(defaultLoadPath + "." + parserName).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static WebsiteNovelParser getParser(WebsiteIdentifier identifier) {
        return getParser(identifier.name());
    }

    public static List<WebsiteNovelParser> getWebsiteNovelParsers() {
        List<WebsiteNovelParser> parsers = new ArrayList<>();
        Class<WebsiteIdentifier> clazz = WebsiteIdentifier.class;
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            WebsiteNovelParser parser = getParser(field.getName());
            if (parser != null) {
                parsers.add(parser);
            }
        }
        return parsers;
    }

}
