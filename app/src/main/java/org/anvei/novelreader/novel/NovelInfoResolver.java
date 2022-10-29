package org.anvei.novelreader.novel;

import org.anvei.novelreader.model.Novel;

public interface NovelInfoResolver {

    Novel resolve(String path);

}
