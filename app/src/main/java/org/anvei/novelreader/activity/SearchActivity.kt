package org.anvei.novelreader.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import org.anvei.novelreader.R

class SearchActivity : BaseActivity() {

    private lateinit var backButton: ImageButton

    private lateinit var searchEdit: EditText

    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        backButton = findViewById(R.id.searchBack)
        searchEdit = findViewById(R.id.searchEdit)
        searchButton = findViewById(R.id.searchButton)

        backButton.setOnClickListener{
            finish()
        }

        searchButton.setOnClickListener{
            val intent = Intent(this, NovelListActivity::class.java)
            intent.putExtra(EXTRA_SEARCH_KEYWORD, searchEdit.text.toString())
            startActivity(intent)
        }
    }

}