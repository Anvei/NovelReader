package org.anvei.novelreader.utils;

import java.io.File;

/**
 * 缓存文件生成规则:<br/>
 * 网站标识符数字形式 + 网站标识符首字母 + 作者名加密 + NovelUID
 */
public final class FileDuplicateUtils {

    public static enum WebsiteIdentifier {
        SFACG,
        BIQUMU
    }

    /**
     * 将网站标识符转成数字加一个首字母
     */
    private static String encrypt(WebsiteIdentifier identifier) {
        String s = identifier.toString().toLowerCase();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++ ) {
            builder.append((int)s.charAt(i));
        }
        builder.append(s.charAt(0));
        return builder.toString();
    }

    public static int getNovelUID(String novelName) {
        return novelName.hashCode();
    }



}
