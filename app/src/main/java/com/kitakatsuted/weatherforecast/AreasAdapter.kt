package com.kitakatsuted.weatherforecast

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AreasAdapter(private val context: Context, private val areas: List<Area>) : RecyclerView.Adapter<AreasAdapter.AreaViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun getItemCount(): Int = areas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {
        // Viewを生成する
        val view: View = inflater.inflate(R.layout.list_registratable_location_row, parent, false)
        // ViewHolderを生成する
        val viewHolder: AreaViewHolder = AreaViewHolder(view)

        return viewHolder
    }

    // viewに表示すべき値を設定する
    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        // アダプター中の位置に応じた記事を得る
        val area = areas[position]
        //記事のタイトルを設定する
        holder.areaTitle.text = area.title

        val cityListAdapter = CityListAdapter(context, area.cities)
        holder.cities.adapter = cityListAdapter

        setListViewHeightBasedOnChildren(holder.cities)

        // Viewタップ時の処理
        holder.cities.setOnItemClickListener { _, _, position, _ ->
            val city: City = cityListAdapter.getItem(position)
            context.getSharedPreferences("choicedLocation", Context.MODE_PRIVATE).edit().putString("locationId", city.id).apply()

            val intent = Intent(context, ForecastActivity::class.java)
            context.startActivity(intent)
//            Toast.makeText(context, "登録地域が${city.title}に変更されました", Toast.LENGTH_SHORT).show()
        }
    }

    // ビューホルダー
    class AreaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val areaTitle = view.findViewById<TextView>(R.id.areaTitle)
        val cities = view.findViewById<ListView>(R.id.cityList)
    }

    // 入れ子にしたリストビューのrowによってlistviewのheightが変わる
    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val params : ViewGroup.LayoutParams = listView.layoutParams;

        val listItem = listView.adapter.getView(1, null, listView)
        if (listItem is ViewGroup) {
            listItem.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
        listItem.measure(0, 0)
        println(listItem.measuredHeight)

        params.height = listView.adapter.getCount() * listItem.measuredHeight
        listView.layoutParams = params
    }

}

class CityListAdapter(private val context: Context,
                      private val cities: List<City>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: createView(parent)

        val city: City = getItem(position)

        val viewHolder: ViewHolder = view.tag as ViewHolder

        viewHolder.cityTitle.text = city.title

        return view
    }

    override fun getItem(position: Int): City = cities[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = cities.size

    private fun createView(parent: ViewGroup?) : View {
        val view: View = inflater.inflate(R.layout.list_city_row, parent, false)
        view.tag = ViewHolder(view)
        return view
    }

    private class ViewHolder(view: View) {
        val cityTitle = view.findViewById<TextView>(R.id.cityTitle)
    }

}