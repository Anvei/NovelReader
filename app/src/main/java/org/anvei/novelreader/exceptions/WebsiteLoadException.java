package org.anvei.novelreader.exceptions;

/**
 * 网站小说加载过程中的错误
 */
public class WebsiteLoadException extends IllegalArgumentException {

    public WebsiteLoadException(String s) {
        super(s);
    }
}
