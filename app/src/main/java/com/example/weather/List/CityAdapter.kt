package com.example.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.Entity.City

class CityAdapter (private val onClick: (City) -> Unit) : RecyclerView.Adapter<ListHolder>() {

    var citys: List<City> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return ListHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val city = citys[position]
        holder.bind(city)
    }

    override fun getItemCount(): Int = citys.count()
}

class ListHolder(itemView: View, private val onClick: (City) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    private val nameText: TextView = itemView.findViewById(R.id.nameText)
    private val tempText: TextView = itemView.findViewById(R.id.tempText)

    fun bind(citys: City) {
        nameText.text = citys.name
        tempText.text = itemView.context.getString(R.string.tempText, citys.temp)
        itemView.setOnClickListener { onClick(citys) }
    }
}