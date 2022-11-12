package org.anvei.novelreader.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import org.anvei.novelreader.R
import org.anvei.novelreader.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity() {

    private lateinit var viewBinding: ActivitySearchBinding

    private lateinit var backBtn: ImageButton
    private lateinit var searchEdit: EditText
    private lateinit var searchTextViewBtn: TextView
    private lateinit var deleteHistory: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
    }

    private fun initComponent() {
        viewBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        backBtn = viewBinding.spBackBtn
        searchEdit = viewBinding.spSearchLiner.findViewById(R.id.sp_edit)
        searchTextViewBtn = viewBinding.spSearchLiner.findViewById(R.id.sp_search_btn)
        deleteHistory = viewBinding.spDeleteHistory
        backBtn.setOnClickListener {
            finish()
        }
        searchTextViewBtn.setOnClickListener{
            val keyWord = searchEdit.text
            if (TextUtils.isEmpty(keyWord)) {
                Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, NovelListActivity::class.java)
                intent.putExtra(EXTRA_SEARCH_KEYWORD, searchEdit.text.toString())
                startActivity(intent)
            }
        }
        deleteHistory.setOnClickListener {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()
        }
    }

}