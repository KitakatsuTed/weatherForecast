package com.kitakatsuted.weatherforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import kotlinx.android.synthetic.main.location_forecast_fragment.*

class LocationForecastFragment : Fragment(), LoaderManager.LoaderCallbacks<Forecast> {

    val DEFAULT_LOCATION = "130010"

    private lateinit var locationID: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.location_forecast_fragment, container, false)
        val forecastTitle = view.findViewById<TextView>(R.id.forecastTitle)

        locationID = arguments?.getString("location") ?: "130010" // デフォルトは東京

        LoaderManager.getInstance(this).initLoader(1, null, this)

        return null
    }

    // initLoaderしたら下記が次々に呼ばれるローダーのコールバック
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Forecast> = ForecastLoader(context!!, locationID)

    override fun onLoadFinished(loader: Loader<Forecast>, data: Forecast?) {
        if (data != null) {
            forecastTitle.setText(data.title)


            // RecyclerViewをレイアウトから探す
//            val recyclerView = findViewById<RecyclerView>(R.id.articles)

            // RSSの記事一覧のアダプター
//            val adapter = ArticlesAdapter(this, data.articles) { article ->
                // 記事をタップした時の処理
//                val intent: CustomTabsIntent = CustomTabsIntent.Builder().build()
//                intent.launchUrl(this, Uri.parse(article.link))
//            }

//            recyclerView.adapter = adapter

            // グリッド表示するレイアウトマネージャー
//            val layoutManager = GridLayoutManager(this, 2)

//            recyclerView.layoutManager = layoutManager

            LoaderManager.getInstance(this).destroyLoader(loader.getId())
        }
    }

    override fun onLoaderReset(loader: Loader<Forecast>) {
    }
}