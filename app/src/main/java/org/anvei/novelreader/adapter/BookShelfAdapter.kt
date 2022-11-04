package org.anvei.novelreader.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.anvei.novelreader.R
import org.anvei.novelreader.activity.BaseActivity
import org.anvei.novelreader.activity.ReadPageActivity
import org.anvei.novelreader.disk.BookShelfHelper
import org.anvei.novelreader.model.NovelInfo

class BookShelfAdapter(val list: List<NovelInfo>, val context: Context) :
    RecyclerView.Adapter<BookShelfAdapter.Holder>() {

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        val cover: ImageView = view.findViewById(R.id.bookShelfItemCover)
        val novelName: TextView = view.findViewById(R.id.bookShelfItemName)
        val author: TextView = view.findViewById(R.id.bookShelfItemAuthor)
        val newestChapter: TextView = view.findViewById(R.id.bookShelfItemNewestChapter)
        val delete: ImageButton = view.findViewById(R.id.bookShelfItemDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.book_shelf_item, parent, false)
        return Holder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val novelInfo = list[position]
        if (novelInfo.cachePic != null) {
            Glide.with(context).load(novelInfo.cachePic).into(holder.cover)
        }  else if (novelInfo.picUrl != null) {
            Glide.with(context).load(novelInfo.picUrl).into(holder.cover)
        }
        holder.novelName.text = novelInfo.novel.name
        holder.author.text = novelInfo.novel.author
        if (novelInfo.newestChapter != null) {
            holder.newestChapter.text = "最新章节: ${novelInfo.newestChapter.chapter.name}"
        }
        holder.view.setOnClickListener {
            ReadPageActivity.startActivity(context, novelInfo)
        }
        holder.delete.setOnClickListener {
            val database = BookShelfHelper(context, "anvei", null, 1).writableDatabase
            database.delete("BookShelfItem", "website = ? and novel = ?", arrayOf(novelInfo.identifier.name, novelInfo.novel.name))
        }
    }

    override fun getItemCount() = list.size
}
