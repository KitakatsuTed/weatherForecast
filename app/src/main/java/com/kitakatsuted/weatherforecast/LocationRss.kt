package com.kitakatsuted.weatherforecast

import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.InputStream
import java.util.*
import javax.xml.XMLConstants
import javax.xml.namespace.NamespaceContext
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory
import javax.xml.XMLConstants.DEFAULT_NS_PREFIX
import javax.xml.XMLConstants.XML_NS_PREFIX
import javax.xml.XMLConstants.XML_NS_URI
import javax.xml.XMLConstants.NULL_NS_URI
import java.util.Arrays.asList





data class LocationRss(val areas: List<Area>)

data class Area(val title: String, val cities: List<City>)

data class City(val title: String, val id: String)

class LocationRssLoader(context: Context) : AsyncTaskLoader<LocationRss>(context) {

    private var cache : LocationRss? = null

    override fun loadInBackground(): LocationRss? {
        val stream = httpGetToStream(getLocationRssLink())
        println(getLocationRssLink())

        if (stream != null) return parseLocationRss(stream)
            // 取得に成功したら、パースして返す

        return null
    }

    override fun deliverResult(data: LocationRss?) {
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

    private fun getLocationRssLink() : String = "http://weather.livedoor.com/forecast/rss/primary_area.xml"

    fun parseLocationRss(stream: InputStream) : LocationRss {
        // XMLをDOMオブジェクトにする
        val documentBuilderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
        documentBuilderFactory.isNamespaceAware = true

        val doc: Document = documentBuilderFactory.newDocumentBuilder().parse(stream)
        stream.close()

        val xPath: XPath = XPathFactory.newInstance().newXPath()

        xPath.namespaceContext = object : NamespaceContext {
            override fun getPrefixes(arg0: String): Iterator<*>? {
                return null
            }

            override fun getPrefix(arg0: String): String? {
                return null
            }

            override fun getNamespaceURI(arg0: String): String? {
                return if ("ldWeather" == arg0) {
                    "http://weather.livedoor.com/%5C/ns/rss/2.0"
                } else null
            }
        }

        val prefNodes: NodeList = xPath.evaluate("/rss/channel/ldWeather:source//pref", doc, XPathConstants.NODESET) as NodeList

        val areas = arrayListOf<Area>()

        for (i in 0 until prefNodes.length) {
            val prefNode: Node = prefNodes.item(i)
            val cityNodes = xPath.evaluate(".//city", prefNode, XPathConstants.NODESET) as NodeList

            val cities = arrayListOf<City>()
            for(ii in 0 until cityNodes.length) {
                val cityNode: Node = cityNodes.item(ii)
                val city = City(
                    title = xPath.evaluate("./@title", cityNode),
                    id = xPath.evaluate("./@id", cityNode)
                )
                cities.add(city)
            }

            val area = Area(xPath.evaluate(("./@title"), prefNode), cities)
            areas.add(area)
        }

        return LocationRss(areas)
    }


}