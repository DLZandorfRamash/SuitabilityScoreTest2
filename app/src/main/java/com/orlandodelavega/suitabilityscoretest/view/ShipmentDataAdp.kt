package com.orlandodelavega.suitabilityscoretest.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.orlandodelavega.suitabilityscoretest.MainActivity
import com.orlandodelavega.suitabilityscoretest.R

class ShipmentDataAdp(private val data: ArrayList<String>, private val curScreen: MainActivity) : RecyclerView.Adapter<ShipmentDataAdp.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_list_selector, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.btnData.text = data[position]
        holder.btnData.setOnClickListener {
            curScreen.selectedShipmentPos = position
            curScreen.updatePreview()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val btnData: Button = view.findViewById(R.id.btnData)
    }

    override fun getItemCount(): Int = data.size
}