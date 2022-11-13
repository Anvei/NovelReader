package org.anvei.novelreader.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import org.anvei.novelreader.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity() {

    private lateinit var viewBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
    }

    private fun initComponent() {
        viewBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.spBackBtn.setOnClickListener {
            finish()
        }
        viewBinding.spSearchBar.setBtnOnClickListener {
            if (TextUtils.isEmpty(it) || it.toString().trim() == "") {
                toast("不能为空")
            } else {
                val intent = Intent(this, NovelListActivity::class.java)
                intent.putExtra(EXTRA_SEARCH_KEYWORD, it)
                startActivity(intent)
            }
        }
        viewBinding.spDeleteHistory.setOnClickListener {
            toast("清除成功")
        }
    }

}