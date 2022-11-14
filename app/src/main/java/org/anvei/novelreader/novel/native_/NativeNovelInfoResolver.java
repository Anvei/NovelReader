package org.anvei.novelreader.novel.native_;

import org.anvei.novelreader.beans.NativeNovel;

public interface NativeNovelInfoResolver {

    NativeNovel resolve(String path);

}
