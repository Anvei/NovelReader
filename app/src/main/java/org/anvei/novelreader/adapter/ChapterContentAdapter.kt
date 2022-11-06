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

class ChapterContentAdapter(private val list: List<ChapterInfo>, private val activity: ReadPageActivity,
    private val novelParser: WebsiteNovelParser) : RecyclerView.Adapter<ChapterContentAdapter.Holder>() {

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.readChapterTitle)
        val content: TextView = view.findViewById(R.id.readContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(activity).inflate(R.layout.chapter_content_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val chapterInfo = list[position]
        activity.setCurrentIndex(chapterInfo.index)
        holder.title.text = chapterInfo.chapter.name
        Thread {
            val chapter = novelParser.loadChapter(chapterInfo)
            activity.runOnUiThread {
                holder.content.text = chapter.content
            }
            Log.d("Anvei", "${chapterInfo.index}")
        }.start()
        holder.content.setOnClickListener {
            activity.onSettingViewClick()
        }
    }

    override fun getItemCount() = list.size
}