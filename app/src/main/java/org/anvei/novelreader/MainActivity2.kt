package org.anvei.novelreader

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.anvei.novelreader.activity.ReadPageActivity
import org.anvei.novelreader.model.NovelInfo
import org.anvei.novelreader.novel.SfacgParser


class MainActivity2 : AppCompatActivity() {

    private lateinit var search: Button

    private lateinit var editText: EditText

    private lateinit var textView: TextView

    private lateinit var selectFirst: Button

    private lateinit var novelInfoList: List<NovelInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        search = findViewById(R.id.search)
        editText = findViewById(R.id.editText)
        textView = findViewById(R.id.text2)
        selectFirst = findViewById(R.id.selectFirst)

        search.setOnClickListener {
            if (editText.text.isNotEmpty()) {
                try {
                    novelInfoList = SfacgParser().search(editText.text.toString())
                    val stringBuilder = StringBuilder()
                    novelInfoList.forEach{
                        stringBuilder.append("《").append(it.novel.name).append("》").append("\n")
                            .append(" 作者: ").append(it.novel.author).append("\n")
                            .append(" 简介: ").append("\n    ").append(it.introduction).append("\n\n")
                    }
                    textView.text = stringBuilder.toString()
                } catch (ex: Exception) {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    ex.printStackTrace()
                }
            }
        }

        selectFirst.setOnClickListener {
            if (novelInfoList != null) {
                val novelInfo = novelInfoList[0]
                val intent = Intent(this, ReadPageActivity::class.java)
                intent.putExtra("novelUrl", novelInfo.url)
                intent.putExtra("novelName", novelInfo.novel.name)
                startActivity(intent)
            }
        }

    }
}