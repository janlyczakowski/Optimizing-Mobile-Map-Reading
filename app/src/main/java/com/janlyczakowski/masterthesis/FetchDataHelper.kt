package com.janlyczakowski.masterthesis

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject


class FetchDataHelper {

fun GET(url: String, callback: Callback): Call {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .build()

    val call = client.newCall(request)
    call.enqueue(callback)
    return call
}

    fun convertToGeoJSON(data: JSONObject): JSONObject {
        val geojson = JSONObject()
        geojson.put("type", "FeatureCollection")
        val features = JSONArray()


        val elements = data.getJSONArray("elements")


        for (i in 0 until elements.length()) {
            val item = elements.getJSONObject(i)
            val feature = JSONObject()
            feature.put("type", "Feature")
            feature.put("id", item.getString("id"))

            val geometry = JSONObject()
            geometry.put("type", "Point")
            val coordinates = if (item.getString("type") == "node") {
                JSONArray().put(item.getDouble("lon")).put(item.getDouble("lat"))
            } else {
                val center = item.getJSONObject("center")
                JSONArray().put(center.getDouble("lon")).put(center.getDouble("lat"))
            }
            geometry.put("coordinates", coordinates)
            feature.put("geometry", geometry)

            val properties = JSONObject()
            properties.put("id", item.getString("id"))
            val tags = item.getJSONObject("tags")
            properties.put("function", getValue(tags, "function"))
            properties.put("icon", getValue(tags, "icon"))
            properties.put("latitude", if (item.getString("type") == "node") item.getDouble("lat") else item.getJSONObject("center").getDouble("lat"))
            properties.put("longitude", if (item.getString("type") == "node") item.getDouble("lon") else item.getJSONObject("center").getDouble("lon"))
            feature.put("properties", properties)

            features.put(feature)
        }

        geojson.put("features", features)
        return geojson
    }


    private val poi = PoiHelper()

    // Some POI have more than one category (e.g. amenity=restaurant, building=yes)
// Take only the category that has a valid property
    fun getValue(tags: JSONObject, returnType: String): String {
        var correctValue: String? = null
        var correctIcon: String? = null

        for (tag in tags.keys()) {
            val existingCategory = poi.iconCategories.find { category -> category == tag }
            // once the category is found, search for correct value (there are correct categories but with wrong values)
            if (existingCategory != null) {
                poi.icons.find { icon ->
                    if (icon.functions.contains(tags.getString(tag))) {
                        correctValue = tags.getString(tag)
                        correctIcon = icon.path
                        return@find true
                    } else if (tag == "shop") {
                        when (tags.getString(tag)) {
                            "supermarket" -> {
                                correctValue = "supermarket"
                                correctIcon = "R.drawable.supermarket"
                            }
                            "bakery", "pastry" -> {
                                correctValue = "bakery"
                                correctIcon = "R.drawable.bakery"
                            }
                            else -> {
                                correctValue = "shop"
                                correctIcon = "R.drawable.shop"
                            }
                        }
                        return@find true
                    }
                    else if (tags.getString(tag) == "pitch" || tags.getString(tag) == "sport_centre" || tags.getString(tag) == "fitness_centre" || tags.getString(tag) == "fitness_station" || tags.getString(tag) == "sports_centre"){
                        correctValue = "sport_fitness"
                        correctIcon = "R.drawable.sport_fitness"
                        return@find true
                    }
                    else if (tag == "office") {
                        correctValue = "office"
                        correctIcon = "R.drawable.office"
                        return@find true
                    }
                    false
                }
                if (correctValue != null) {
                    break
                }
            }
        }
        return if (returnType == "icon") correctIcon?.split('.')?.last() ?: "" else correctValue ?: ""
    }

}