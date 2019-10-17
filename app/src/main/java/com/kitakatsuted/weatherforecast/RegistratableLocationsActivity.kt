package com.kitakatsuted.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RegistratableLocationsActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<LocationRss> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registratable_locations)

        LoaderManager.getInstance(this).initLoader(2, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<LocationRss> = LocationRssLoader(this)

    override fun onLoadFinished(loader: Loader<LocationRss>, data: LocationRss?) {
        if (data != null) {

            // RecyclerViewをレイアウトから探す
            val recyclerView = findViewById<RecyclerView>(R.id.registratableLocationList)

            // RSSの記事一覧のアダプター
            val adapter = AreasAdapter(this, data.areas)

            recyclerView.adapter = adapter

            // グリッド表示するレイアウトマネージャー
            val layoutManager = GridLayoutManager(this, 1)

            recyclerView.layoutManager = layoutManager
        }


        LoaderManager.getInstance(this).destroyLoader(loader.getId())
    }

    override fun onLoaderReset(loader: Loader<LocationRss>) {}
}