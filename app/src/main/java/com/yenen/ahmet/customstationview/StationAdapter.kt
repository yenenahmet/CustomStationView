package com.yenen.ahmet.customstationview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yenen.ahmet.stationview.StationView

class StationAdapter(listener: StationAdapterListener) : RecyclerView.Adapter<StationAdapter.ViewHolder>(){


    private var lastStationView: StationView? = null
    private var lastPosition:Int =0

    private var litener: StationAdapterListener? = null

    private val items: MutableList<Boolean>

    init {
        this.litener = listener
        items = mutableListOf()
        for (i in 0..10) {
            items.add(false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_station, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return 10
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
        holder.stationView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (v != null && v is StationView) {
                    lastItem(position, v)

                    setChanged(position,true)
                    litener!!.onItemClick(position)
                }
            }
        })

    }

    private fun lastItem(position: Int, view: StationView) {
        if (lastStationView != null) {
            setChanged(lastPosition,false)
        }
        lastStationView = view
        lastPosition = position
    }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val stationView = view.findViewById<StationView>(R.id.stationView)

        fun bind(item:Boolean){
            if(item){
                stationView.startCircleAnimator()
            }else{
                stationView.pauseCircleAnimator()
                stationView.changeCenterCircleColor(stationView.centerCirclePrimaryColor)
            }
        }
    }

    fun setChanged(position: Int, item: Boolean) {
        this.items[position] = item
        notifyItemChanged(position, item)
    }


    // gc clear lastStationView
    private fun unBindLastStationView() {
        lastStationView?.unBind()
        lastStationView = null
    }

    fun unBind() {
        items.clear()
        litener = null
        unBindLastStationView()
    }
}