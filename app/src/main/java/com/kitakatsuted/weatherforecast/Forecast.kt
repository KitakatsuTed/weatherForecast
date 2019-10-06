package com.kitakatsuted.weatherforecast

import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class Forecast(val title: String, val description: String, val currentTime: Date, val weathers: List<Weather>)

data class Weather(val dateLabel: String, val telop: String, val date: String, val minTemp: String?, val maxTemp: String?)

class ForecastLoader(context: Context, val locationId: String) : AsyncTaskLoader<Forecast>(context) {

    private var cache : Forecast? = null

    override fun loadInBackground(): Forecast? {
        val jsonObject = httpGet(generateApiUrl(locationId))

        if (jsonObject != null) {
            // 取得に成功したら、パースして返す
            return parseForecast(jsonObject)
        }

        return null
    }

    override fun deliverResult(data: Forecast?) {
//         破棄されていたら結果を返さない
        if (isReset || data == null) return

//         結果をキャッシュする
        cache = data
        super.deliverResult(data)
    }

    override fun onStartLoading() {
        //バックグラウンド処理が開始される前に呼ばれる
        if (cache != null) deliverResult(cache)

        // コンテンツが変化している場合やキャッシュがない場合には、バックグラウンド処理を使う
        if (takeContentChanged() || cache == null) forceLoad()
    }

    // ローダーが停止する前に呼ばれる処理
    override fun onStopLoading() { cancelLoad() }

    // ローダーが破棄される前に呼ばれる処理
    override fun onReset() {
        super.onReset()
        onStartLoading()
        cache = null
    }

    private fun generateApiUrl(locationId: String) = "http://weather.livedoor.com/forecast/webservice/json/v1?city=${locationId}"

    private fun parseForecast(jsonObject: JSONObject): Forecast {
        val weathers = arrayListOf<Weather>()

        val datas = jsonObject.getJSONArray("forecasts")

        for (i in (0..datas.length() - 1)) {
            val forecastJson: JSONObject = datas.getJSONObject(i)
            val temp = forecastJson.getJSONObject("temperature")

            val weather = Weather(
                forecastJson.getString("dateLabel"),
                forecastJson.getString("telop"),
                forecastJson.getString("date"),
                if (temp.isNull("min")) "No Data" else temp.getJSONObject("min").getString("celsius"),
                if (temp.isNull("max")) "No Data" else temp.getJSONObject("max").getString("celsius")
            )

            weathers.add(weather)
        }

        return Forecast(
            jsonObject.getString("title"),
            jsonObject.getJSONObject("description").getString("text"),
            parseToDate(jsonObject.getString("publicTime")) ,
            weathers)
    }

    private fun parseToDate(dateString: String): Date {
        val pattern = "yyyy-MM-dd'T'HH:mm:ssX"
        return SimpleDateFormat(pattern).parse(dateString)
    }
}