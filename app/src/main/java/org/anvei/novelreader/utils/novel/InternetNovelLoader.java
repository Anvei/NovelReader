package org.anvei.novelreader.utils.novel;

import org.anvei.novelreader.model.Novel;

import java.net.MalformedURLException;
import java.net.URL;

public class InternetNovelLoader implements NovelLoader {

    private URL url;

    private InternetNovelResolver resolver;

    public InternetNovelLoader(URL url, InternetNovelResolver resolver) {
        this.url = url;
        this.resolver = resolver;
    }

    public InternetNovelLoader(String path, InternetNovelResolver resolver) throws MalformedURLException {
        this(new URL(path), resolver);
    }

    @Override
    public Novel load() {

        return null;
    }

    public Novel load(URL url) {
        this.url = url;
        return load();
    }

    public Novel load(String path) throws MalformedURLException {
        return load(new URL(path));
    }

    public Novel load(URL url, InternetNovelResolver resolver) {
        this.resolver = resolver;
        this.url = url;
        return load();
    }

    public Novel load(String path, InternetNovelResolver resolver) throws MalformedURLException {
        return load(new URL(path), resolver);
    }

}
