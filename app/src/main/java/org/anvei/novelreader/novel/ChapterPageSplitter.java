package org.anvei.novelreader.novel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 这里的切割假设txt文件格式比较规范，规范如下：<br/>
 * <li>每个段落只有末尾有换行符</li>
 * <li>每个段落前面已经有段落首行缩进</li>
 */
public class ChapterPageSplitter {

    private final int maxLines;
    private final int maxCharWidthPerLine;

    /**
     * maxLines、maxCharsPerLine参数由外部负责计算出来
     * @param maxLines：最大行数
     * @param maxCharWidthPerLine：一个英文字符一个单位、一个中文字符两个单位
     */
    public ChapterPageSplitter(int maxLines, int maxCharWidthPerLine) {
        this.maxLines = maxLines;
        this.maxCharWidthPerLine = maxCharWidthPerLine;
    }

    public static final int ENG_CHAR_WIDTH = 2;
    public static final int SPECIAL_CHAR_WIDTH = 3;

    // 中文字符以及日文字符的正则表达式
    private static final String twoWidthRegex_1 = "[\\u0800-\\u9fa5]";
    private static final String twoWidthRegex_2 = "[，。？！—（）【】；：“《》、￥]";

    public static boolean isTwoWidth(char ch) {
        return Pattern.matches(twoWidthRegex_1, "" + ch) ||
                Pattern.matches(twoWidthRegex_2, "" + ch);
    }

    /**
     * 截取指定宽度的字符，默认英文字符占1个单位宽度、中文字符占2个单位宽度
     * getStringByWidth()读取最多宽度为maxCharWidthPerLine的字符串
     * 如果给定的index超过了字符串最后一个字符所在的下标就返回null
     */
    public String getStringByWidth(String src, int start, int width) {
        if (start >= src.length())
            return null;
        StringBuilder s = new StringBuilder();
        while (width > 0 && start < src.length()) {
            char ch = src.charAt(start);
            if (isTwoWidth(ch)) {
                // 允许溢出一个单位
                if (width < SPECIAL_CHAR_WIDTH - 1) {
                    break;
                } else {
                    s.append(ch);
                    width -= SPECIAL_CHAR_WIDTH;
                    start++;
                }
            } else {
                s.append(ch);
                width -= ENG_CHAR_WIDTH;
                start++;
            }
        }
        return s.toString();
    }

    public List<String> startSplit(String content) {
        List<String> resultList = new ArrayList<>();
        StringBuilder s = new StringBuilder();

        int linesUsed = 0;

        // 以换行符进行切割获取段落，split字符串中是没有换行符的
        String[] splits = content.split("\n");
        for (String split : splits) {
            String trimStr = split.trim();
            // 如果该行全为空白字符，就过滤掉
            if (trimStr.length() != 0) {
                int pointer = 0;                        // 初始化指针指向起始位置
                String line;
                while ((line = getStringByWidth(split, pointer, maxCharWidthPerLine)) != null) {
                    s.append(line).append("\n");
                    linesUsed++;
                    pointer += line.length();
                    if (linesUsed >= maxLines) {
                        resultList.add(s.toString());
                        s.delete(0, s.length());
                        linesUsed = 0;
                    }
                }
            }
        }
        // 处理最后一页
        if (!(s.length() == 0)) {
            resultList.add(s.toString());
        }
        return resultList;
    }

}
