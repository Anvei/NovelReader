package org.anvei.novelreader.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.anvei.novelreader.R
import org.anvei.novelreader.activity.NovelHomeActivity
import org.anvei.novelreader.beans.WebsiteNovelInfo

class SearchItemAdapter(private val list: List<WebsiteNovelInfo>, private val context: Context)
    : RecyclerView.Adapter<SearchItemAdapter.Holder>() {

    private val itemPosition = R.id.searchItemCover

    class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        val cover: ImageView = view.findViewById(R.id.searchItemCover)
        val novel: TextView = view.findViewById(R.id.searchItemNovel)
        val author: TextView = view.findViewById(R.id.searchItemAuthor)
        val intro: TextView = view.findViewById(R.id.searchItemIntro)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_result_item, parent, false)
        return Holder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val novelInfo = list[position]
        holder.novel.text = novelInfo.novelName
        holder.author.text = "${novelInfo.author} - [${novelInfo.identifier.name}]"
        holder.intro.text = novelInfo.intro
        if (TextUtils.isEmpty(novelInfo.coverUrl)) {
            // 没有封面就给一个默认封面
            Glide.with(context)
                .load(AppCompatResources.getDrawable(context, R.drawable.default_cover))
                .into(holder.cover)
        } else if (holder.cover.getTag(itemPosition) == null) {
            // 第一次加载封面
            Glide.with(context)
                .load(novelInfo.coverUrl)
                .placeholder(R.drawable.default_cover)
                .into(holder.cover)
            holder.cover.setTag(itemPosition, position)
        } else if (position != holder.cover.getTag(itemPosition)) {
            // 处理视图复用
            Glide.with(context)
                .load(novelInfo.coverUrl)
                .placeholder(R.drawable.default_cover)
                .into(holder.cover)
            holder.cover.setTag(itemPosition, position)
        }
        holder.view.setOnClickListener{
            // 启动小说主页界面
            NovelHomeActivity.startActivity(context, novelInfo)
        }
    }

    override fun getItemCount() = list.size
}