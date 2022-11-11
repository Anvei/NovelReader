package org.anvei.novelreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.anvei.novelreader.R
import org.anvei.novelreader.activity.ReadPageActivity
import org.anvei.novelreader.beans.WebsiteChapterInfo

class ChapterListAdapter(private val list: List<WebsiteChapterInfo>, private val activity: ReadPageActivity) :
    RecyclerView.Adapter<ChapterListAdapter.Holder>() {

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
        holder.textView.text = chapterInfo.chapterName
        holder.textView.setOnClickListener {
            activity.onCurrentIndex(position)
            activity.onCurrentChapter()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}