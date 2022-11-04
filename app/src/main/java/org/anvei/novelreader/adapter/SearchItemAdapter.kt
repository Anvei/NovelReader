package org.anvei.novelreader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.anvei.novelreader.R
import org.anvei.novelreader.activity.NovelHomeActivity
import org.anvei.novelreader.model.NovelInfo

class SearchItemAdapter(private val list: List<NovelInfo>, private val context: Context)
    : RecyclerView.Adapter<SearchItemAdapter.Holder>() {

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.searchItemCover)
        val novel: TextView = view.findViewById(R.id.searchItemNovel)
        val author: TextView = view.findViewById(R.id.searchItemAuthor)
        val brief: TextView = view.findViewById(R.id.searchItemBrief)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val novelInfo = list[position]
        holder.novel.text = novelInfo.novel.name
        holder.author.text = novelInfo.novel.author
        holder.brief.text = novelInfo.introduction

        /*holder.image.setTag(R.id.searchItemCover, novelInfo.picUrl)
        // TODO: 处理异步加载情况下，RecyclerView图片错乱问题
        if (holder.image.getTag(R.id.searchItemCover) != null &&
                holder.image.getTag(R.id.searchItemCover).equals(novelInfo.picUrl)) {
            Glide.with(context).load(novelInfo.picUrl).into(holder.image)
        }*/

        holder.view.setOnClickListener{
            // 启动小说主页界面
            NovelHomeActivity.startActivity(context, novelInfo)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}