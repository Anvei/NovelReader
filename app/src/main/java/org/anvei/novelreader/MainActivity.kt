package org.anvei.novelreader

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import org.anvei.novelreader.activity.BaseActivity
import org.anvei.novelreader.novel.NativeNovelLoader
import org.anvei.novelreader.utils.FileUtils
import java.io.File

class MainActivity : BaseActivity() {

    @SuppressLint("SetTextI18n")
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            val file: File = File(externalCacheDir, "temp.txt")
            FileUtils.copy(file, contentResolver.openInputStream(it))
            val novel = NativeNovelLoader(file)
                .load()
            findViewById<TextView>(R.id.text).text = novel?.getChapter(1)?.name + "\n" +
                    novel?.getChapter(1)?.content
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.select).setOnClickListener {
            launcher.launch("*/*")
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, MainActivity2::class.java))
            }
        }
        return true
    }
}