package org.anvei.novelreader.novel;

import org.anvei.novelreader.model.NovelInfo;

import java.util.List;

public interface NovelSearchFilter {

    List<NovelInfo> filter(List<NovelInfo> novelInfoList);

}
