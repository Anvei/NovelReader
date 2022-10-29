package org.anvei.novelreader.novel;

// 顶层加载器抽象
public interface Loader<E> {

    E load();

}
