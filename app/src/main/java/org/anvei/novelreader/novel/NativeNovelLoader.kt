
package org.anvei.novelreader.novel
/*
import org.anvei.novelreader.beans.Chapter
import org.anvei.novelreader.beans.Novel
import java.io.*
import java.util.*

*/
/**
 * 本地小说加载器的默认实现
 *//*

class NativeNovelLoader : NovelLoader {
  */
/*  private var file: File
    private var novelInfoResolver: NativeNovelInfoResolver
    private var chapterSplitter: ChapterSplitter? = null

    constructor(path: String) : this(File(path)) {}
    constructor(file: File) {
        this.file = file
        novelInfoResolver = DefaultNovelInfoResolver()
    }

    constructor(file: File, novelInfoResolver: NativeNovelInfoResolver) {
        this.file = file
        this.novelInfoResolver = novelInfoResolver
    }

    *//*
*/
/**
     * 从本地文件中加载小说主要的主要函数
     * @return 返回加载获得的Novel对象
     *//*
*/
/*
    override fun load(): Novel? {
        var novel: Novel? = null
        try {
            if (chapterSplitter == null) {
                chapterSplitter = DefaultChapterSplitter(FileInputStream(file))
            }
            novel = novelInfoResolver.resolve(file.path)
            while (chapterSplitter!!.hasMoreElements()) {
                novel.addChapter(chapterSplitter!!.nextElement())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return novel
    }

    fun load(file: File): Novel? {
        this.file = file
        return load()
    }

    fun load(path: String): Novel? {
        file = File(path)
        return load()
    }

    *//*
*/
/**
     * 设置小说信息（小说名称，小说作者）解析器
     *//*
*/
/*
    fun setNovelInfoResolver(novelInfoResolver: NativeNovelInfoResolver) {
        this.novelInfoResolver = novelInfoResolver
    }

    fun setChapterSplitter(chapterSplitter: ChapterSplitter?) {
        this.chapterSplitter = chapterSplitter
    }

    *//*
*/
/**
     * 本地小说章节分割器的默认实现
     *//*
*/
/*
    class DefaultChapterSplitter(protected val `in`: InputStream) : ChapterSplitter {
        protected val chapterList: MutableList<Chapter> = ArrayList()
        protected var index // 指向当前nextElement()返回的元素
                : Int

        // 判断该行是否是章节名
        protected fun idChapterStart(line: String): Boolean {
            // 考虑空行的情况
            if (line != "" && line[0] == '第') {
                val index = line.indexOf('章')
                if (index == -1) return false

                // 如果index = 1，返回的是一个空字符串（长度为0）
                val chapterNum = line.substring(1, index)

                // 处理数字章节号
                var digitalFlag = true
                for (i in 0 until chapterNum.length) {
                    if (!Character.isDigit(chapterNum[i])) {
                        digitalFlag = false
                        break
                    }
                }
                if (digitalFlag) return true

                // 简单的处理一下中文章节号，如果“第”之后的一个字符都是中文章节号文字就直接返回true
                val set: Set<Char> = HashSet(
                    Arrays.asList(
                        '一', '二', '三', '四', '五', '六',
                        '七', '八', '九', '十'
                    )
                )
                return set.contains(chapterNum[0])
            }
            return false
        }

        // 解析章节
        @Throws(IOException::class)
        protected fun parse() {
            val reader = BufferedReader(
                InputStreamReader(
                    `in`
                )
            )
            var line: String
            var temp: String
            val stringBuilder = StringBuilder()
            var chapter: Chapter
            var name: String? = null
            while (reader.readLine().also { line = it } != null) {
                temp = line.trim { it <= ' ' }
                if (idChapterStart(temp)) {
                    if (name != null) {
                        // 假定章节名独占一行
                        chapter = Chapter(name, stringBuilder.toString())
                        chapterList.add(chapter)
                        // 清空StringBuilder
                        stringBuilder.delete(0, stringBuilder.length)
                    }
                    name = temp
                } else {
                    stringBuilder.append("    ").append(temp).append("\n")
                }
            }

            // 处理最后一章
            if (name != null) {
                chapter = Chapter(name, stringBuilder.toString())
                chapterList.add(chapter)
            }
        }

        override fun hasMoreElements(): Boolean {
            return index != chapterList.size
        }

        override fun nextElement(): Chapter {
            val chapter = chapterList[index]
            ++index
            return chapter
        }

        init {
            parse()
            index = 0
        }
    }

    *//*
*/
/**
     * 本地小说信息解析器的默认实现
     *//*
*/
/*
    class DefaultNovelInfoResolver : NativeNovelInfoResolver {
        override fun resolve(path: String): Novel {
            // 切割文件后缀名
            val fileName = path.substring(0, path.lastIndexOf('.'))
            val index = fileName.lastIndexOf('-')
            val novelName: String
            val author: String
            if (index != -1) {
                novelName = fileName.substring(0, index) // 由文件名解析小说名和作者
                author = fileName.substring(index + 1)
            } else {
                novelName = fileName
                author = ""
            }
            return Novel(novelName, author)
        }
    }*//*

}*/
