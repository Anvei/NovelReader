package org.anvei.novelreader.dao

import androidx.room.*
import org.anvei.novelreader.beans.WebsiteNovel

@Dao
interface WebsiteNovelDao {
    @Insert
    fun addNovel(novel: WebsiteNovel)

    @Delete
    fun deleteNovel(novel: WebsiteNovel)

    @Update
    fun updateNovel(novel: WebsiteNovel)

    @Query("SELECT * from ${WebsiteNovel.tableName}")
    fun queryAllNovel(): List<WebsiteNovel>

    @Query("select * from ${WebsiteNovel.tableName} " +
            "where website == :website and author = :author and novel_name == :novelName")
    fun queryNovel(website: String, author: String?, novelName: String): WebsiteNovel?
}