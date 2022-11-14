package org.anvei.novelreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.anvei.novelreader.R
import org.anvei.novelreader.activity.ReadPageActivity
import org.anvei.novelreader.beans.WebsiteChapter

class ChapterListAdapter(private val list: List<WebsiteChapter>, private val activity: ReadPageActivity) :
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
        if (position == activity.getCurrentIndex()) {
            holder.textView.setTextColor(activity.resources
                .getColor(R.color.rp_chapter_list_item_checked, null))
        } else {
            // 处理视图重用造成的多个章节名显示为红色问题
            holder.textView.setTextColor(activity.resources
                .getColor(R.color.rp_chapter_list_item_unchecked, null))
        }
        holder.textView.setOnClickListener {
            val lastIndex = activity.getCurrentIndex()
            activity.onCurrentChapter(position)
            activity.setCurrentIndex(position)
            notifyItemChanged(lastIndex)
            notifyItemChanged(position)
            activity.onCurrentChapterName()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}