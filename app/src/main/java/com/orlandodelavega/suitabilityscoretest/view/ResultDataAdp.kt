package com.orlandodelavega.suitabilityscoretest.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.orlandodelavega.suitabilityscoretest.R
import com.orlandodelavega.suitabilityscoretest.data.ResultsData

class ResultDataAdp(private val data: ArrayList<ResultsData>) : RecyclerView.Adapter<ResultDataAdp.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_list_result, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.txtDriverResult.text = data[position].driver
        holder.txtShipmentResult.text = data[position].shipment
        holder.txtScoreResult.text = data[position].score.toString()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val txtDriverResult: TextView = view.findViewById(R.id.txtDriverResult)
        val txtShipmentResult: TextView = view.findViewById(R.id.txtShipmentResult)
        val txtScoreResult: TextView = view.findViewById(R.id.txtScoreResult)
    }

    override fun getItemCount(): Int = data.size
}