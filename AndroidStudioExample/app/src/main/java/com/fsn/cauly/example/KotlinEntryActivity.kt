package com.fsn.cauly.example

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class KotlinEntryActivity : AppCompatActivity() {
    var listview: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_entry)
        listview = findViewById<View>(R.id.native_area) as ListView
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, type)
        listview!!.adapter = myAdapter
        listview!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            if (position == 0) startActivity(
                Intent(
                    this@KotlinEntryActivity,
                    KotlinActivity::class.java
                )
            ) else if (position == 1) startActivity(
                Intent(
                    this@KotlinEntryActivity,
                    KotlinXMLActivity::class.java
                )
            ) else if (position == 2) startActivity(
                Intent(
                    this@KotlinEntryActivity,
                    KotlinNativeViewActivity::class.java
                )
            ) else if (position == 3) startActivity(
                Intent(
                    this@KotlinEntryActivity,
                    KotlinNativeDataActivity::class.java
                )
            ) else startActivity(Intent(this@KotlinEntryActivity, KotlinCloseAdActivity::class.java))
        }
    }

    companion object {
        val type = arrayOf(
            "동적 배너및 전면",
            "XML배너 및 전면",
            "네이티브 뷰타입",
            "네이티브 데이터 타입",
            "종료팝업"
        )
    }
}