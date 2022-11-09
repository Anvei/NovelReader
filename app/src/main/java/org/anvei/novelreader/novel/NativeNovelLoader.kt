package org.anvei.novelreader.novel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.anvei.novelreader.model.Chapter;
import org.anvei.novelreader.model.Novel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 本地小说加载器的默认实现
 */
public class NativeNovelLoader implements NovelLoader {

    private File file;

    private NativeNovelInfoResolver novelInfoResolver;

    private ChapterSplitter chapterSplitter;

    public NativeNovelLoader(@NonNull String path) {
        this(new File(path));
    }

    public NativeNovelLoader(@NonNull File file) {
        this.file = file;
        this.novelInfoResolver = new DefaultNovelInfoResolver();
    }

    public NativeNovelLoader(@NonNull File file, @NonNull NativeNovelInfoResolver novelInfoResolver) {
        this.file = file;
        this.novelInfoResolver = novelInfoResolver;
    }

    /**
     * 从本地文件中加载小说主要的主要函数
     * @return 返回加载获得的Novel对象
     */
    @Override
    public @Nullable Novel load() {
        Novel novel = null;
        try {
            if (chapterSplitter == null) {
                chapterSplitter = new DefaultChapterSplitter(new FileInputStream(file));
            }
            novel = novelInfoResolver.resolve(file.getPath());
            while (chapterSplitter.hasMoreElements()) {
                novel.addChapter(chapterSplitter.nextElement());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return novel;
    }

    public @Nullable Novel load(@NonNull File file) {
        this.file = file;
        return load();
    }

    public @Nullable Novel load(@NonNull String path) {
        file = new File(path);
        return load();
    }

    /**
     * 设置小说信息（小说名称，小说作者）解析器
     */
    public void setNovelInfoResolver(NativeNovelInfoResolver novelInfoResolver) {
        this.novelInfoResolver = novelInfoResolver;
    }

    public void setChapterSplitter(ChapterSplitter chapterSplitter) {
        this.chapterSplitter = chapterSplitter;
    }

    /**
     * 本地小说章节分割器的默认实现
     */
    public static class DefaultChapterSplitter implements ChapterSplitter {

        protected final InputStream in;

        protected final List<Chapter> chapterList = new ArrayList<>();

        protected int index;          // 指向当前nextElement()返回的元素

        public DefaultChapterSplitter(@NonNull InputStream in) throws IOException {
            this.in = in;
            parse();
            index = 0;
        }

        // 判断该行是否是章节名
        protected boolean idChapterStart(@NonNull String line) {
            // 考虑空行的情况
            if (!line.equals("") && line.charAt(0) == '第') {
                int index = line.indexOf('章');
                if (index == -1)
                    return false;

                // 如果index = 1，返回的是一个空字符串（长度为0）
                String chapterNum = line.substring(1, index);

                // 处理数字章节号
                boolean digitalFlag = true;
                for (int i = 0; i < chapterNum.length(); i++) {
                    if (!Character.isDigit(chapterNum.charAt(i))) {
                        digitalFlag = false;
                        break;
                    }
                }
                if (digitalFlag)
                    return true;

                // 简单的处理一下中文章节号，如果“第”之后的一个字符都是中文章节号文字就直接返回true
                Set<Character> set = new HashSet<>(Arrays.asList('一', '二', '三', '四', '五', '六',
                        '七', '八', '九', '十'));
                return set.contains(chapterNum.charAt(0));
            }
            return false;
        }

        // 解析章节
        protected void parse() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            String temp;
            StringBuilder stringBuilder = new StringBuilder();
            Chapter chapter;
            String name = null;
            while ((line = reader.readLine()) != null) {
                temp = line.trim();
                if (idChapterStart(temp)) {
                    if (name != null) {
                        // 假定章节名独占一行
                        chapter = new Chapter(name, stringBuilder.toString());
                        chapterList.add(chapter);
                        // 清空StringBuilder
                        stringBuilder.delete(0, stringBuilder.length());
                    }
                    name = temp;
                } else {
                    stringBuilder.append("    ").append(temp).append("\n");
                }
            }

            // 处理最后一章
            if (name != null) {
                chapter = new Chapter(name, stringBuilder.toString());
                chapterList.add(chapter);
            }
        }

        @Override
        public boolean hasMoreElements() {
            return index != chapterList.size();
        }

        @Override
        public @NonNull Chapter nextElement() {
            Chapter chapter = chapterList.get(index);
            ++index;
            return chapter;
        }
    }

    /**
     * 本地小说信息解析器的默认实现
     */
    public static class DefaultNovelInfoResolver implements NativeNovelInfoResolver {

        @Override
        public Novel resolve(String path) {
            // 切割文件后缀名
            String fileName = path.substring(0, path.lastIndexOf('.'));
            int index = fileName.lastIndexOf('-');
            String novelName;
            String author;
            if (index != -1) {
                novelName = fileName.substring(0, index);    // 由文件名解析小说名和作者
                author = fileName.substring(index + 1);
            } else {
                novelName = fileName;
                author = "";
            }
            return new Novel(novelName, author);
        }

    }

}
