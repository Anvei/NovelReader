package org.anvei.novelreader.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.anvei.novelreader.R
import org.anvei.novelreader.model.ChapterInfo
import org.anvei.novelreader.novel.WebsiteNovelParser

class ChapterItemAdapter(private val list: List<ChapterInfo>, private val activity: Activity,
    private val novelParser: WebsiteNovelParser
) : RecyclerView.Adapter<ChapterItemAdapter.Holder>() {

    private val chapterContentView: TextView = activity.findViewById(R.id.readContent)

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
                    chapterContentView.text = chapter.content
                    // 给当前章节设置选中的颜色
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        holder.textView.setTextColor(activity.resources.getColor(R.color.chapter_list_item_clicked_text_color, null))
//                    } else {
//                        holder.textView.setTextColor(activity.resources.getColor(R.color.chapter_list_item_clicked_text_color))
//                    }
                    // 定位到顶部，有BUG，需要调用两次才能保证滑动到顶部
                    activity.findViewById<ScrollView>(R.id.scrollContent).fullScroll(View.FOCUS_UP)
                    activity.findViewById<ScrollView>(R.id.scrollContent).fullScroll(View.FOCUS_UP)
                }
            }.start()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}