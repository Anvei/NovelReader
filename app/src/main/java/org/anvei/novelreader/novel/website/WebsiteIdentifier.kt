package org.anvei.novelreader.novel.website

enum class WebsiteIdentifier {
    UNKNOWN,  // 未知网站标识符
    SFACG,
    BIQUMU,
    W147XS;

    companion object {
        /**
         * 根据网站表示符的字符形式，获取一个WebsiteIdentifier变量
         */
        @JvmStatic
        fun getIdentifier(str: String): WebsiteIdentifier? {
            val str1 = str.uppercase()
            var res: WebsiteIdentifier? = null
            val clazz: Class<*> = WebsiteIdentifier::class.java
            val fields = clazz.fields
            for (field in fields) {
                if (str1 == field.name) {
                    try {
                        res = field[null] as WebsiteIdentifier
                        break
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                }
            }
            return res
        }
    }
}