package com.kitakatsuted.weatherforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class LocationForecastFragment : Fragment(), LoaderManager.LoaderCallbacks<Forecast> {

    val DEFAULT_LOCATION = "130010"

    private lateinit var locationID: String
    // コールバックを超えてあたいの共有したいけど、これであってんのか？
    private lateinit var forecastTitle: TextView
    private lateinit var forecastDescription: TextView
    private lateinit var forecastDatetime: TextView
    private lateinit var forecastDatesLabel: TextView
    private lateinit var descriptionScroll: ScrollView
    private lateinit var weatherList: ListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.location_forecast_fragment, container, false)
        forecastTitle = view.findViewById<TextView>(R.id.forecastTitle)
        forecastDescription = view.findViewById<TextView>(R.id.forecastDescription)
        forecastDatetime = view.findViewById<TextView>(R.id.forecastDatetime)
        forecastDatesLabel = view.findViewById<TextView>(R.id.forecastDatesLabel)
        descriptionScroll = view.findViewById<ScrollView>(R.id.descriptionScroll)
        weatherList = view.findViewById<ListView>(R.id.weatherList)

        if (arguments?.getString("location") != null) {
            locationID = arguments?.getString("location")!!
            LoaderManager.getInstance(this).initLoader(1, null, this)
            return view
        }

        return null
    }

    // initLoaderしたら下記が次々に呼ばれるローダーのコールバック
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Forecast> = ForecastLoader(context!!, locationID)

    override fun onLoadFinished(loader: Loader<Forecast>, data: Forecast?) {
        if (data != null) {
            forecastTitle.text = data.title
            println(data.description.javaClass)
            forecastDescription.text = data.description
            // ここは日付でパースしたい
            forecastDatetime.text = "予報時刻:${data.currentTime}"

            // weatherクラスに格納する時点でパースしても良さそう、それだとdateはDateクラスにしないといけない
//            val format = SimpleDateFormat("yyyy-MM-dd")
//            forecastDatesLabel.text = "${data.weathers.map { format.parse(it.date) }.joinToString(separator = ",")}+の予報"
            val dates = data.weathers.map { it.date }
            forecastDatesLabel.text = "${dates.joinToString(separator = ",")}の予報"

            weatherList.adapter = WeatherListAdapter(context!!, data.weathers)

            LoaderManager.getInstance(this).destroyLoader(loader.getId())
        }
    }

    override fun onLoaderReset(loader: Loader<Forecast>) {
    }
}