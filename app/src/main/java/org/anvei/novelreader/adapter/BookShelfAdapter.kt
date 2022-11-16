package org.anvei.novelreader.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.anvei.novelreader.R
import org.anvei.novelreader.activity.MainActivity
import org.anvei.novelreader.activity.ReadPageActivity
import org.anvei.novelreader.beans.WebsiteNovel
import org.anvei.novelreader.room.repository.NovelRepository
import java.util.*

/**
 * 书架上的小说视图Adapter
 * 负责展示封面、小说名、最后阅读时间、最新章节、以及小说设置按钮点击事件的处理
 */
class BookShelfAdapter(val list: MutableList<WebsiteNovel>,
                       val activity: MainActivity) :
    RecyclerView.Adapter<BookShelfAdapter.Holder>() {

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        val cover: ImageView = view.findViewById(R.id.bsi_cover)
        val novelName: TextView = view.findViewById(R.id.bsi_name)
        val lastReadTime: TextView = view.findViewById(R.id.bsi_last_read_time)
        val lastChapter: TextView = view.findViewById(R.id.bsi_last_chapter)
        val setting: ImageButton = view.findViewById(R.id.bsi_setting)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(activity).inflate(R.layout.book_shelf_item, parent, false)
        return Holder(view)
    }

    // TODO: 尽量别再onBindViewHolder()中设置监听事件
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val novel = list[position]
        // 加载封面
        if (novel.coverCache != null) {
          Glide.with(activity).load(novel.novelCache).into(holder.cover)
        } else if (novel.coverUrl != null) {
            Glide.with(activity).load(novel.coverUrl).into(holder.cover)
        } else {
            // TODO: 加载默认封面
        }
        // 设置最后阅读时间的显示信息
        holder.novelName.text = novel.novelName
        if (novel.lastReadTime != null) {
            val interval = Date().time - novel.lastReadTime.time
            if (interval <   12 * 60 * 60 * 1000) {
                val hours = interval / (1000 * 60 * 60)
                if (hours > 0) {
                    holder.lastReadTime.text = "最后阅读时间: ${hours}小时前"
                } else {
                    holder.lastReadTime.text = "最后阅读时间: 一小时内"
                }
            } else {
                if (interval < 1000 * 60 * 60 * 24) {
                    holder.lastReadTime.text = "最后阅读时间: 今天"
                } else {
                    holder.lastReadTime.text = "最后阅读时间: ${interval / (1000 * 60 * 60 * 24)}天前"
                }
            }
        } else {
            holder.lastReadTime.text = "尚未阅读"
        }
        // TODO: 处理最新章节信息的显示
        if (novel.lastChapterName != null) {
            holder.lastChapter.text = "最新章节: ${novel.lastChapterName}"
        }
        // 设置点击事件：打开小说阅读界面
        holder.view.setOnClickListener {
            ReadPageActivity.startActivity(activity, novel)
        }
        // 小说设置按钮的点击事件
        // TODO: 采用PopupWindows时处理更多选项，暂时是作为将小说从书架中删除的临时按钮
        holder.setting.setOnClickListener {
            Thread {
                NovelRepository.deleteNovelFromBookShelf(novel)
                activity.runOnUiThread {
                    activity.deleteNovel(position)
                    activity.toast("删除成功")
                }
            }.start()
        }
    }

    override fun getItemCount() = list.size
}
