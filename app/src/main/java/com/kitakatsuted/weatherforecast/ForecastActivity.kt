package com.kitakatsuted.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment

class ForecastActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        // 動的にフラグメントを配置する際にはすでに配置されているかの確認が必要 supportFragmentManager を使う
        if (supportFragmentManager.findFragmentByTag("location_forecast_fragment") == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, newLabelFragment(getDefautLocation()), "location_forecast_fragment")
                .commit()
        }
    }

    private fun getDefautLocation(): String {
        // @see http://weather.livedoor.com/forecast/rss/primary_area.xml
        // ここは位置情報からidを決めたい
        return "130010" // 東京
    }
}

// これは共通化の方法がありそう
fun newLabelFragment(location: String): Fragment {
    val fragment = LocationForecastFragment()

    val args = Bundle()
    args.putString("location", location)

    fragment.arguments = args

    return fragment
}