package com.kitakatsuted.weatherforecast

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LocationIndexFragment : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (arguments?.getSerializable("forecast") == null) return null
        val view = inflater.inflate(R.layout.location_index_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.locationIndex)
        val forecast: Forecast = arguments?.getSerializable("forecast") as Forecast
        val adapter = LocationsAdapter(context!!, forecast.locations) {
            val parentActivity = this.activity
            if (parentActivity != null) {
                // ここでwebviewをだすコールバックを仕込む
//                Toast.makeText(context, "${it.name}", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView.adapter = adapter
        // グリッド表示するレイアウトマネージャー
        val layoutManager = GridLayoutManager(context, 2)

        recyclerView.layoutManager = layoutManager
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        arguments?.putSerializable("forecast", null)
    }

}