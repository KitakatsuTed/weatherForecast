package com.kitakatsuted.weatherforecast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class WeatherListAdapter(private val context: Context, private val weathers: List<Weather>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: createView(parent)

        val weather: Weather = getItem(position)

        val viewHolder: ViewHolder = view.tag as ViewHolder

        viewHolder.dateLabel.text = weather.date
        viewHolder.telop.text = weather.telop
        viewHolder.minTemp.text = weather.minTemp
        viewHolder.maxTemp.text = weather.maxTemp

        return view
    }

    override fun getItem(position: Int): Weather = weathers[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = weathers.size

    private fun createView(parent: ViewGroup?) : View {
        val view: View = inflater.inflate(R.layout.list_weather_row, parent, false)
        view.tag = ViewHolder(view)
        return view
    }

    private class ViewHolder(view: View) {
        val dateLabel = view.findViewById<TextView>(R.id.dateLabel)
        val telop = view.findViewById<TextView>(R.id.telop)
        val minTemp = view.findViewById<TextView>(R.id.minTemp)
        val maxTemp = view.findViewById<TextView>(R.id.maxTemp)
    }

}