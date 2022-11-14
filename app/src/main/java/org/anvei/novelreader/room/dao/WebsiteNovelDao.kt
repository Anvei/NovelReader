package org.anvei.novelreader.room.dao

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

    @Query("select * from ${WebsiteNovel.tableName} where uid = :uid")
    fun queryNovelByUid(uid: Long): WebsiteNovel?

    @Query("select * from ${WebsiteNovel.tableName} where author = :author and novel_name = :novelName")
    fun queryByAuthorAndNovelName(author: String, novelName: String): WebsiteNovel?

}