package org.anvei.novelreader.novel;

import org.anvei.novelreader.beans.Novel;

public interface NativeNovelInfoResolver {

    Novel resolve(String path);

}
