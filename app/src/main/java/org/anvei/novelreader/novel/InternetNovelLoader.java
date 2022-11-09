package org.anvei.novelreader.novel;

import org.anvei.novelreader.beans.Novel;

/**
 * implementation of the web novel loader<br/><br/>
 */
public class InternetNovelLoader implements NovelLoader {

    protected String key;

    protected InternetNovelLoader(String key) {
        this.key = key;
    }

    @Override
    public Novel load() {
        return null;
    }

}
