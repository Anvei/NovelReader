package org.anvei.novelreader.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.anvei.novelreader.R
import org.anvei.novelreader.activity.BaseActivity
import org.anvei.novelreader.activity.ReadPageActivity
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_result_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val novelInfo = list[position]
        holder.novel.text = novelInfo.novel.name
        holder.author.text = novelInfo.novel.author
        holder.brief.text = novelInfo.introduction

        if (novelInfo.picUrl != null) {
            Glide.with(context).load(novelInfo.picUrl).into(holder.image)
        }

        holder.view.setOnClickListener{
            // 启动小说主页界面
            val intent = Intent(context, ReadPageActivity::class.java)
            intent.putExtra(BaseActivity.EXTRA_NOVEL_HOME_NAME, novelInfo.novel.name)
            intent.putExtra(BaseActivity.EXTRA_NOVEL_HOME_URL, novelInfo.url)
            intent.putExtra(BaseActivity.EXTRA_NOVEL_HOME_AUTHOR, novelInfo.novel.author)
            intent.putExtra(BaseActivity.EXTRA_NOVEL_HOME_BRIEF, novelInfo.introduction)
            intent.putExtra(BaseActivity.EXTRA_NOVEL_HOME_COVER, novelInfo.picUrl)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}