package org.anvei.novelreader.activity

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import org.anvei.novelreader.R
import org.anvei.novelreader.adapter.ChapterItemAdapter
import org.anvei.novelreader.novel.SfacgParser

class ReadPageActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var chapterContent: TextView

    private lateinit var chapterList: RecyclerView

    // private lateinit var  toolbar: Toolbar

    private val parser = SfacgParser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_page)

        supportActionBar?.apply {
            hide()
        }

        drawerLayout = findViewById(R.id.readPageDrawer)
        chapterContent = findViewById(R.id.chapterContent)

        val headView = findViewById<NavigationView>(R.id.navigation)
            .getHeaderView(0)

        chapterList = headView.findViewById(R.id.chapterPage_chapterList)

        drawerLayout.setScrimColor(Color.TRANSPARENT)

        val novelUrl = intent.getStringExtra("novelUrl")

        val chapterInfoList = parser.loadChapterList(novelUrl)

        chapterContent.text = parser.loadChapter(chapterInfoList[0]).content

        chapterList.adapter = ChapterItemAdapter(chapterInfoList, this)
        chapterList.layoutManager = LinearLayoutManager(this)

        headView.findViewById<TextView>(R.id.chapterPage_novelName).text = intent.getStringExtra("novelName")

        findViewById<FloatingActionButton>(R.id.floatingButton).setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.openDrawer(GravityCompat.START)
    }

}