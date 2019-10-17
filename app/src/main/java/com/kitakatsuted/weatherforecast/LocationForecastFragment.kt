package com.kitakatsuted.weatherforecast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class LocationForecastFragment : Fragment(), LoaderManager.LoaderCallbacks<Forecast> {

    val DEFAULT_LOCATION = "130010"

    private lateinit var locationId: String
    // コールバックを超えてあたいの共有したいけど、これであってんのか？
    private lateinit var forecastTitle: TextView
    private lateinit var forecastDescription: TextView
    private lateinit var forecastDatetime: TextView
    private lateinit var forecastDatesLabel: TextView
    private lateinit var descriptionScroll: ScrollView
    private lateinit var changeLocationButton: TextView
    private lateinit var weatherList: ListView

    //    フラグメント呼び出し元でリスナーの実装が無い場合は例外
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is OnChangeButtonCllickListener)
            throw RuntimeException("リスナーを実装してください")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.location_forecast_fragment, container, false)
        forecastTitle = view.findViewById<TextView>(R.id.forecastTitle)
        forecastDescription = view.findViewById<TextView>(R.id.forecastDescription)
        forecastDatetime = view.findViewById<TextView>(R.id.forecastDatetime)
        forecastDatesLabel = view.findViewById<TextView>(R.id.forecastDatesLabel)
        descriptionScroll = view.findViewById<ScrollView>(R.id.descriptionScroll)
        weatherList = view.findViewById<ListView>(R.id.weatherList)
        changeLocationButton = view.findViewById<Button>(R.id.changeLocationButton)

        if (arguments?.getString("locationId") != null) {
            locationId = arguments?.getString("locationId")!!
            LoaderManager.getInstance(this).initLoader(1, null, this)
            return view // ここって非同期処理が成功したかどうかを調べなくて良い？
        }

        return null
    }

    interface OnChangeButtonCllickListener {
        fun onButtonClicked(locationList: Forecast)
    }

    // initLoaderしたら下記が次々に呼ばれるローダーのコールバック
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Forecast> = ForecastLoader(context!!, locationId)

    override fun onLoadFinished(loader: Loader<Forecast>, data: Forecast?) {
        if (data != null) {
            forecastTitle.text = data.title
            forecastDescription.text = data.description
            // ここは日付でパースしたい
            forecastDatetime.text = "予報時刻:${data.currentTime}"

            // weatherクラスに格納する時点でパースしても良さそう、それだとdateはDateクラスにしないといけない
//            val format = SimpleDateFormat("yyyy-MM-dd")
//            forecastDatesLabel.text = "${data.weathers.map { format.parse(it.date) }.joinToString(separator = ",")}+の予報"
            val dates = data.weathers.map { it.date }
            forecastDatesLabel.text = "${dates.joinToString(separator = ",")}の予報"

            weatherList.adapter = WeatherListAdapter(context!!, data.weathers)

            setListener(data)
            LoaderManager.getInstance(this).destroyLoader(loader.getId())
        }
    }

    override fun onLoaderReset(loader: Loader<Forecast>) {
    }

    private fun setListener(forecast: Forecast) {
        changeLocationButton.setOnClickListener {
            val listener = context as? OnChangeButtonCllickListener
            listener?.onButtonClicked(forecast)
        }
    }
}