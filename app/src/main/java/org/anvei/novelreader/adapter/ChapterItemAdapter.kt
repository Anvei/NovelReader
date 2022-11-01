package org.anvei.novelreader.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.anvei.novelreader.R
import org.anvei.novelreader.model.ChapterInfo
import org.anvei.novelreader.novel.BiqumuParser

class ChapterItemAdapter(private val list: List<ChapterInfo>, private val activity: Activity)
    : RecyclerView.Adapter<ChapterItemAdapter.Holder>() {

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
            val parser = BiqumuParser()
            val chapter = parser.loadChapter(chapterInfo)
            activity.findViewById<TextView>(R.id.chapterContent).text = chapter.content
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}