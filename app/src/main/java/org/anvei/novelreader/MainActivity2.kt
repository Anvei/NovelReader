package org.anvei.novelreader

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.anvei.novelreader.novel.SfacgNovelLoader


class MainActivity2 : AppCompatActivity() {

    private lateinit var search: Button

    private lateinit var editText: EditText

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        search = findViewById(R.id.search)
        editText = findViewById(R.id.editText)
        textView = findViewById(R.id.text2)

        search.setOnClickListener {
            if (editText.text.isNotEmpty()) {
                try {
                    val novelInfoList = SfacgNovelLoader.search(editText.text.toString())
                    val stringBuilder = StringBuilder()
                    novelInfoList.forEach{
                        stringBuilder.append(it.novel.name).append(" 作者: ").append(it.novel.author).append("\n")
                    }
                    textView.text = stringBuilder.toString()
                } catch (ex: Exception) {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    ex.printStackTrace()
                }
            }
        }
    }
}