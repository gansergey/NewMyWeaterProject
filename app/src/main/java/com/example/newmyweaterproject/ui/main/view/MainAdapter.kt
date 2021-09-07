package com.example.newmyweaterproject.ui.main.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newmyweaterproject.R
import com.example.newmyweaterproject.ui.main.model.Weather


class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var weatherData: List<Weather> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: OnItemViewClickListener? = null

    //set выше делает тоже самое
    //fun setWeather(weather: List<Weather>){
    //weatherData = weather
    //notifyDataSetChanged()
    //}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.main_fragment_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            itemView.findViewById<TextView>(R.id.city_name).text = weather.city.name
            itemView.setOnClickListener {
                listener?.onItemClick(weather)
            }
        }
    }

    //fun - функциональный интерфейс. Если один метод то можно преобразовать в лямду
    fun interface OnItemViewClickListener {
        fun onItemClick(weather: Weather)
    }
}