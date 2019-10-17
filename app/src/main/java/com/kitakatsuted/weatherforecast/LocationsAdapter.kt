package com.kitakatsuted.weatherforecast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LocationsAdapter(context: Context,
                       private val locations: List<Location>,
                       private val onLocationClicked: (Location) -> Unit)
    : RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun getItemCount(): Int = locations.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        // Viewを生成する
        val view: View = inflater.inflate(R.layout.location_index_row, parent, false)
        // ViewHolderを生成する
        val viewHolder: LocationViewHolder = LocationViewHolder(view)

        // Viewタップ時の処理
        view.setOnClickListener{
            // タップされた記事の位置
            val position: Int = viewHolder.adapterPosition
            // タップされた位置に応じた記事
            val article = locations[position]
            // コールバックを呼ぶ
            onLocationClicked(article)
        }

        return viewHolder
    }

    // viewに表示すべき値を設定する
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        // アダプター中の位置に応じた記事を得る
        val location = locations[position]
        //記事のタイトルを設定する
        holder.locationName.text = location.name
    }

    // ビューホルダー
    class LocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val locationName = view.findViewById<TextView>(R.id.locationName)
    }
}