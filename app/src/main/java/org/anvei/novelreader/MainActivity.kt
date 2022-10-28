package org.anvei.novelreader

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import org.anvei.novelreader.utils.novel.NativeNovelLoader
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            val file: File = File(externalCacheDir, "temp.txt")
            cacheNovel(file, it)
            val novel = NativeNovelLoader(file)
                .load()
            findViewById<TextView>(R.id.text).text = novel?.getChapter(1)?.name + "\n" +
                    novel?.getChapter(1)?.content
        }
    }

    // 缓存小说
    private fun cacheNovel(file: File, uri: Uri) {
        if (file.exists()) {
            file.delete()
        }
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        file.createNewFile()
        val b: ByteArray = ByteArray(1024)
        while (inputStream?.read(b) != -1) {
            outputStream.write(b)
        }

        inputStream.close()
        outputStream.close()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.select).setOnClickListener {
            launcher.launch("*/*")
        }
    }
}