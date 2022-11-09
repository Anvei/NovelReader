package org.anvei.novelreader.novel

import java.util.*

/* 为了降低耦合度，采用了反射机制进行加载NovelParser */
object NovelParserFactory {
    private const val defaultLoadPath = "org.anvei.novelreader.novel.parser"

    private fun getParserForFieldName(fieldName: String): String {
        return fieldName[0].toString() +
                fieldName.substring(1).lowercase(Locale.getDefault()) +
                "Parser"
    }

    private fun getParser(identifierName: String): WebsiteNovelParser? {
        var res: WebsiteNovelParser? = null
        val parserName = getParserForFieldName(identifierName)
        try {
            res = Class.forName("$defaultLoadPath.$parserName")
                .newInstance() as WebsiteNovelParser
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    fun getParser(identifier: WebsiteIdentifier): WebsiteNovelParser {
        return getParser(identifier.name)!!
    }

    val websiteNovelParsers: List<WebsiteNovelParser>
        get() {
            val parsers: MutableList<WebsiteNovelParser> = ArrayList()
            val clazz = WebsiteIdentifier::class.java
            val fields = clazz.fields
            for (field in fields) {
                if (field.name != "UNKNOWN") {
                    val parser = getParser(field.name)
                    if (parser != null) {
                        parsers.add(parser)
                    }
                }
            }
            return parsers
        }
}