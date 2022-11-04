package org.anvei.novelreader.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.anvei.novelreader.R
import org.anvei.novelreader.activity.ReadPageActivity
import org.anvei.novelreader.model.ChapterInfo
import org.anvei.novelreader.novel.WebsiteNovelParser

class ChapterItemAdapter(private val list: List<ChapterInfo>, private val activity: ReadPageActivity,
    private val novelParser: WebsiteNovelParser
) : RecyclerView.Adapter<ChapterItemAdapter.Holder>() {

    private val chapterContentView: TextView = activity.findViewById(R.id.readContent)

    private val chapterTitleView: TextView = activity.findViewById(R.id.readChapterTitle)

    private var lastIndex = -1          // 记录上一个章节号

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.chapterListItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.chapter_list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val chapterInfo = list[position]
        holder.textView.text = chapterInfo.chapter.name
        holder.textView.setOnClickListener {
            Thread {
                val chapter = novelParser.loadChapter(chapterInfo)
                activity.runOnUiThread {
                    chapterTitleView.text = chapter.name
                    chapterContentView.text = chapter.content
                    // TODO: 给当前章节设置选中的颜色
                    lastIndex = activity.getCurrentIndex()
                    this.notifyItemChanged(lastIndex - 1)
                    Log.d("Anvei", "lastIndex: $lastIndex, currentIndex: ${chapterInfo.index}")
                    activity.setCurrentIndex(chapterInfo.index)

                    holder.textView.setTextColor(activity.resources
                        .getColor(R.color.chapter_list_item_clicked_text_color))

                    activity.scrollChapterContentToTop()
                }
            }.start()
        }
        if (lastIndex == position) {
            Log.d("Anvei", "Change: $lastIndex")
            holder.textView.setTextColor(activity.resources
                .getColor(R.color.text_color))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}