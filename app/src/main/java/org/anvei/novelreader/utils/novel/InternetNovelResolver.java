package org.anvei.novelreader.utils.novel;

import org.anvei.novelreader.model.Novel;

import java.net.URL;
import java.util.List;

public abstract class InternetNovelResolver {

    private URL url;

    protected InternetNovelResolver(URL url) {
        this.url = url;
    }

    public List<Novel> resolve() {
        return null;
    }

    protected abstract List<URL> search();

}
