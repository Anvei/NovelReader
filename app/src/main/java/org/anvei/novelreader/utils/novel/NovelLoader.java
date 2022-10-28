package org.anvei.novelreader.utils.novel;

import org.anvei.novelreader.model.Novel;

public interface NovelLoader extends Loader<Novel> {

    Novel load();

}
