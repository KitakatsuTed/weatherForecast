package com.kitakatsuted.weatherforecast

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment

class ForecastActivity : AppCompatActivity(), LocationForecastFragment.OnChangeButtonCllickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        val configLocationButton = findViewById<TextView>(R.id.configLocationButton)

        configLocationButton.setOnClickListener {
            val intent = Intent(this, RegistratableLocationsActivity::class.java)
            startActivity(intent)
        }

        // 動的にフラグメントを配置する際にはすでに配置されているかの確認が必要 supportFragmentManager を使う
        if (supportFragmentManager.findFragmentByTag("location_forecast_fragment") == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, newLabelFragment(getSharedLocation()), "location_forecast_fragment")
                .commit()
        }
    }

    override fun onButtonClicked(forecast: Forecast) {
        if (supportFragmentManager.findFragmentByTag("location_index_fragment") == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, newLocationIndexFragment(forecast), "location_index_fragment")
                .commit()
        }
        // フラグメントのリプレイスにミスった時どうする？
    }

    // @see http://weather.livedoor.com/forecast/rss/primary_area.xml
    private fun getSharedLocation(): String {
        // ここは位置情報からidを決めたい
        val pref: SharedPreferences = getSharedPreferences("choicedLocation", Context.MODE_PRIVATE)
        if (pref.getString("locationId", "130010") == null) {
            getSharedPreferences("choicedLocation", Context.MODE_PRIVATE).edit().putString("locationId", "130010").apply()
        }
        return pref.getString("locationId", "130010") as String
    }
}

// これは共通化の方法がありそう
fun newLabelFragment(locationId: String): Fragment {
    val fragment = LocationForecastFragment()

    val args = Bundle()
    args.putString("locationId", locationId)

    fragment.arguments = args

    return fragment
}

fun newLocationIndexFragment(forecast: Forecast) : Fragment {
    val fragment = LocationIndexFragment()

    val args = Bundle()
    args.putSerializable("forecast", forecast)

    fragment.arguments = args

    return fragment
}