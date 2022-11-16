package org.anvei.novelreader.novel;

import androidx.annotation.IntRange;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ChapterSplitter {

    public enum ErrorType {
        ENCODING_ERROR,             // 文件编码错误
        IO_ERROR                    // IO异常
    }

    // 默认匹配章节标题的正则表达式
    private static final String defaultChapterTittleRegex = "第.{1,9}[章节回].*";

    private final RandomAccessFile randomAccessFile;

    private final List<String> chapterTitleRegexs = new ArrayList<>();       // 章节名匹配正则表达式

    private final List<ChapterSplitResult> chapterSplitResults = new ArrayList<>();

    private OnSplitResultListener splitResultListener;

    private OnTittleMatchListener tittleMatchListener;

    public ChapterSplitter(File file) throws FileNotFoundException {
        randomAccessFile = new RandomAccessFile(file, "r");
    }

    public String getRegex(int position) {
        return chapterTitleRegexs.get(position);
    }

    public int getRegexCount() {
        return chapterTitleRegexs.size();
    }

    public void removeRegex(int position) {
        chapterTitleRegexs.remove(position);
    }

    public void addRegex(String regex) {
        chapterTitleRegexs.add(regex);
    }

    // 匹配全部正则表达式
    private boolean matchChapterTitle(String line, int index) {
        boolean res = true;
        for (String regex : chapterTitleRegexs) {
            if (!Pattern.matches(regex, line))
                res = false;
        }
        if (res && tittleMatchListener != null) {
            tittleMatchListener.onTittleMatch(line, index);
        }
        return res;
    }

    protected long getCurrentPointer() throws IOException {
        return randomAccessFile.getFilePointer();
    }

    protected String readLine() throws IOException {
        String s = randomAccessFile.readLine();
        if (s == null)
            return null;
        byte[] bytes = s.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes);
    }

    // 调用该函数来开始切割文件获取章节，该方法比较耗时
    // TODO: 处理文件编码问题
    public void startSplit() throws FileFormatException, IOException {

        // 初始化
        randomAccessFile.seek(0);
        chapterSplitResults.clear();
        if (chapterTitleRegexs.isEmpty()) {
            chapterTitleRegexs.add(defaultChapterTittleRegex);
        }

        String chapterName = null;
        long start = -1;
        long last = -1;
        try {
            String line;
            while ((line = readLine()) != null) {
                String trimStr = line.trim();
                if (trimStr.length() == 0)
                    continue;
                if (matchChapterTitle(line, chapterSplitResults.size() + 1)) {
                    // 当下一章的章节名确定时，上一章的所有信息就可以保存了
                    if (chapterName != null && start != -1) {
                        chapterSplitResults.add(new ChapterSplitResult(chapterName, chapterSplitResults.size() + 1,
                                start, last));
                    }
                    chapterName = trimStr;
                    start = getCurrentPointer();        // 此时指针已经指向章节名下一行首字符
                }
                last = getCurrentPointer();
            }
            // 处理最后一章
            if (chapterName != null && start != -1) {
                chapterSplitResults.add(new ChapterSplitResult(chapterName, chapterSplitResults.size() + 1,
                        start, last));
            }
            // 加载成功回调
            if (splitResultListener != null) {
                splitResultListener.onSuccess(chapterSplitResults.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 加载失败回调
            if (splitResultListener != null) {
                splitResultListener.onFail(ErrorType.IO_ERROR);
            }
        }
    }

    public void setOnSplitResultListener(OnSplitResultListener listener) {
        this.splitResultListener = listener;
    }

    public interface OnSplitResultListener {
        void onSuccess(int chapterCount);
        void onFail(ErrorType errorType);
    }

    public void setChapterTitleMatchCallback(OnTittleMatchListener tittleMatchListener) {
        this.tittleMatchListener = tittleMatchListener;
    }

    public interface OnTittleMatchListener {
        /**
         * 当匹配小说章节标题成功时会调用该函数
         * @param titleLine 匹配成功的文本
         * @param index 匹配成功的章节名称的序号
         */
        void onTittleMatch(String titleLine, int index);
    }

    private static final String PARA_PREFIX = "    ";
    private static final String PARA_SUFFIX = "\n";

    public String getChapterContent(@IntRange(from = 1)int chapterIndex) throws IOException {
        chapterIndex = chapterIndex - 1;
        StringBuilder s = new StringBuilder();
        randomAccessFile.seek(chapterSplitResults.get(chapterIndex).getStartIndex());
        String line;
        // 先比较指针，如果还没超过结束位置，就再读取一行
        while(getCurrentPointer() < chapterSplitResults.get(chapterIndex).getEndIndex()
                && (line = readLine()) != null) {
            s.append(line)
                .append(PARA_SUFFIX);
        }
        return s.toString();
    }

    public ChapterSplitResult getChapterSplitResult(@IntRange(from = 1)int chapterIndex) {
        return chapterSplitResults.get(chapterIndex - 1);
    }

}
