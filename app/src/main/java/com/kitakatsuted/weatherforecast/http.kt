package com.kitakatsuted.weatherforecast

import android.graphics.Bitmap
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

fun httpGet(url: String): InputStream? {

    try {
        // 通信接続用のオブジェクトを作る
        val con: HttpURLConnection = URL(url).openConnection() as HttpURLConnection

        // 接続の設定を行う
        con.apply {
            requestMethod = "GET"
            connectTimeout = 3000
            readTimeout = 5000
            instanceFollowRedirects = true
        }

        // 接続
        con.connect()

        // ステータスコードの確認
        if (con.responseCode in 200..299){
            // 成功したら、レスポンスの入力ストリームを、BufferedInputStreamとして返す
            return con.inputStream
        }

        // 失敗
        println("通信に失敗しました。status code: ${con.responseCode}")
        return null
    } catch (e: Exception) {
        println(e)
        throw e
    }
}

fun httpPost(url: String) {
    TODO()
}

fun httpGetToJson(url: String): JSONObject {
    val stream = httpGet(url)
    return parseToJson(BufferedInputStream(stream))
}

fun httpGetToStream(url: String): InputStream {
    return BufferedInputStream(httpGet(url))
}

//fun httpGetToBitmap(url: String): Bitmap {
//
//}

private fun parseToJson(stream: InputStream): JSONObject {
    val reader = BufferedReader(InputStreamReader(stream))
    val buffer = StringBuffer()
    var line: String?

    while (true) {
        line = reader.readLine()
        if (line == null) break
        buffer.append(line)
    }

    //先ほどbufferに入れた、取得した文字列
    val jsonText = buffer.toString()

    // 有限のwifiの接続でhtmlの文字列が帰ってきたらどうしよ？
    return JSONObject(jsonText)
}