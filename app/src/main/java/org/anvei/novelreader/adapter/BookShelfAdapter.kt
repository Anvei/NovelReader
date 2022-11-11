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
import org.anvei.novelreader.beans.WebsiteNovelInfo

/**
 * 书架上的小说视图Adapter
 * 负责展示封面、小说名、最后阅读时间、最新章节、以及小说设置按钮点击事件的处理
 */
class BookShelfAdapter(val list: MutableList<WebsiteNovelInfo>, val activity: MainActivity) :
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val novelInfo = list[position]
        // 加载封面
        if (novelInfo.coverUrl != null) {
            Glide.with(activity).load(novelInfo.coverUrl).into(holder.cover)
        }
        // 设置文字信息
        holder.novelName.text = novelInfo.novelName
        // TODO: 暂时未设置
        holder.lastReadTime.text = "尚未阅读"
        holder.lastChapter.text = "最新章节："
        // 设置点击事件：打开小说阅读界面
        holder.view.setOnClickListener {
            ReadPageActivity.startActivity(activity, novelInfo)
        }
        // 小说设置按钮的点击事件
        // TODO: 采用PopupWindows时处理更多选项，暂时是作为将小说从书架中删除的临时按钮
        holder.setting.setOnClickListener {
            Thread {
                activity.appDatabase.websiteNovelDao.apply {
                    queryNovel(novelInfo.identifier.name, novelInfo.author, novelInfo.novelName)?.let { it1 ->
                        deleteNovel(it1)
                    }
                }
                activity.runOnUiThread {
                    activity.deleteNovel(position)
                }
            }.start()
        }
    }

    override fun getItemCount() = list.size
}
