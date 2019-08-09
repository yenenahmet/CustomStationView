package com.yenen.ahmet.customstationview


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), StationAdapterListener {


    private lateinit var recyclerView: RecyclerView
    // Demo //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = StationAdapter(this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

    }

    private fun selectedScrollToPosition(pos: Int) {
        val manager: GridLayoutManager = recyclerView.layoutManager!! as GridLayoutManager
        manager.scrollToPositionWithOffset(pos - 1, 20);
    }


    override fun onItemClick(postion: Int) {
        selectedScrollToPosition(postion)
    }
}
