package org.anvei.novelreader.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 提供一些文件管理的静态方法
 */
public final class FileUtils {

    private FileUtils() {}

    /**
     * 复制文件
     * @param targetFile 目标文件
     * @param srcFile 源文件
     */
    public static void copy(File targetFile, File srcFile) throws IOException {
        if (targetFile.exists()) {
            targetFile.delete();
        }
        targetFile.createNewFile();
        InputStream is = new FileInputStream(srcFile);
        OutputStream os = new FileOutputStream(targetFile);
        byte[] bytes = new byte[1024];
        while (is.read(bytes) != -1) {
            os.write(bytes);
        }
        is.close();
        os.close();
    }

    public static void copy(File targetFile, InputStream is) throws IOException {
        if (targetFile.exists()) {
            targetFile.delete();
        }
        targetFile.createNewFile();
        OutputStream os = new FileOutputStream(targetFile);
        byte[] bytes = new byte[1024];
        while (is.read(bytes) != -1) {
            os.write(bytes);
        }
        os.close();
    }

    /**
     * 剪切文件
     * @param targetFile 目标文件
     * @param srcFile 源文件
     * @throws IOException 剪切文件失败
     */
    public static void cut(File targetFile, File srcFile) throws IOException {
        copy(targetFile, srcFile);
        srcFile.delete();
    }


}
