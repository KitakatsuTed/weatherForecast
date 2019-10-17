package com.kitakatsuted.weatherforecast

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.loader.content.AsyncTaskLoader

class ImageDownloadTask(context: Context, val imageUrl: String) : AsyncTaskLoader<Bitmap>(context) {

    private var cache : Bitmap? = null

    override fun loadInBackground(): Bitmap? {
        println(imageUrl)

        val stream = httpGetToStream(imageUrl)

        // streamは非null型だけど一応
        if (stream != null) {
            return BitmapFactory.decodeStream(stream)
        }

        return null
    }

    override fun deliverResult(data: Bitmap?) {
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
}