package com.example.earthquake

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list.*
import kotlin.math.floor

class quakeAdapter(val context:Context): RecyclerView.Adapter<quakeAdapter.reportViewHolder>() {
private var reports:ArrayList<report> = ArrayList()
    class reportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mag:TextView  = itemView.findViewById(R.id.mag)
        val place:TextView = itemView.findViewById(R.id.place)
        val date:TextView = itemView.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): reportViewHolder {
     val view = LayoutInflater.from(parent.context).inflate(R.layout.list,parent,false)
        return reportViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reports.size
    }
fun updateReports(newReports:ArrayList<report>){
    reports.clear()
    reports = newReports
    notifyDataSetChanged()
}
    override fun onBindViewHolder(holder: reportViewHolder, position: Int) {
        val magnitudeColor: Int = getMagnitudeColor(reports[position].mag)
        holder.mag.setBackgroundColor(magnitudeColor)
        holder.mag.text = reports[position].mag.toString()
        holder.place.text = reports[position].place
        holder.date.text = reports[position].date
    }
    private fun getMagnitudeColor(magnitude: Double): Int {
        val magnitudeColorResourceId: Int
        val magnitudeFloor = floor(magnitude).toInt()
        magnitudeColorResourceId = when (magnitudeFloor) {
            0, 1 -> R.color.magnitude1
            2 -> R.color.magnitude2
            3 -> R.color.magnitude3
            4 -> R.color.magnitude4
            5 -> R.color.magnitude5
            6 -> R.color.magnitude6
            7 -> R.color.magnitude7
            8 -> R.color.magnitude8
            9 -> R.color.magnitude9
            else -> R.color.magnitude10plus
        }
        return ContextCompat.getColor(context, magnitudeColorResourceId)
    }
}