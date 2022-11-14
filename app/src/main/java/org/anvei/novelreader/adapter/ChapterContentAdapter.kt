package org.anvei.novelreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.anvei.novelreader.AppConfig
import org.anvei.novelreader.R
import org.anvei.novelreader.activity.ReadPageActivity
import org.anvei.novelreader.beans.WebsiteChapter
import org.anvei.novelreader.novel.website.WebsiteNovelParser

class ChapterContentAdapter(
    private val list: List<WebsiteChapter>,
    private val activity: ReadPageActivity,
    private val novelParser: WebsiteNovelParser
) : RecyclerView.Adapter<ChapterContentAdapter.Holder>() {

    private val itemPosition = R.id.readContent

    private var textSize: Float = AppConfig.getNovelContentFontSize()

    fun setContentSize(size: Float) {
        textSize = size
        notifyItemRangeChanged(0, list.size)
    }

    fun getContentSize(): Float{
        return textSize
    }

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.readChapterTitle)
        val content: TextView = view.findViewById(R.id.readContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(activity)
            .inflate(R.layout.chapter_content_item, parent, false)
        view.setOnClickListener {
            activity.onSettingView()
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.content.textSize = textSize
        val chapterInfo = list[position]
        holder.title.text = chapterInfo.chapterName
        // 为null表示是第一次加载
        if (holder.content.getTag(itemPosition) == null) {
            loadNovel(holder, position, chapterInfo)
        } else if (holder.content.getTag(itemPosition) != position){
            // 处理重用视图
            holder.content.text = null
            loadNovel(holder, position, chapterInfo)
        }
    }

    private fun loadNovel(holder: Holder, position: Int, chapter: WebsiteChapter) {
        if (position == activity.getCurrentIndex()) {
            activity.onProgressBar(true)
        }
        Thread {
            novelParser.loadChapter(chapter)
            activity.runOnUiThread {
                if (position == activity.getCurrentIndex()) {
                    activity.onProgressBar(false)
                }
                holder.content.text = chapter.chapterContent
            }
        }.start()
        holder.content.setTag(itemPosition, position)
    }

    override fun getItemCount() = list.size
}