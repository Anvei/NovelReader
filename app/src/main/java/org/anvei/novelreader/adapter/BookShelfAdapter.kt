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
import org.anvei.novelreader.beans.WebsiteNovelInfo

class BookShelfAdapter(val list: List<WebsiteNovelInfo>, val context: Context) :
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
        if (novelInfo.coverUrl != null) {
            Glide.with(context).load(novelInfo.coverUrl).into(holder.cover)
        }
        holder.novelName.text = novelInfo.novelName
        holder.author.text = novelInfo.author
        holder.view.setOnClickListener {
            ReadPageActivity.startActivity(context, novelInfo)
        }
        holder.delete.setOnClickListener {
            val database = BookShelfHelper(context, BaseActivity.DATABASE_NAME, null, 1).writableDatabase
            database.delete(BaseActivity.TABLE_BOOK_SHELF_ITEM, "website = ? and novel = ?", arrayOf(novelInfo.identifier.name, novelInfo.novelName))
        }
    }

    override fun getItemCount() = list.size
}
