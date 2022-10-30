package org.anvei.novelreader.novel;

import org.anvei.novelreader.model.Novel;

public interface NativeNovelInfoResolver {

    Novel resolve(String path);

}
