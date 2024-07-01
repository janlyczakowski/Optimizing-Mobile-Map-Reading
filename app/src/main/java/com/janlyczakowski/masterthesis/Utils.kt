package com.janlyczakowski.masterthesis


import android.content.Context
import android.graphics.BitmapFactory
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.match
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.turf.TurfConstants
import com.mapbox.turf.TurfMeasurement
import java.io.Serializable


class Utils {

    private fun getResourceId(context: Context, path: String): Int {
        val name = path.split(".").last() // get the resource name from the path
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }

    fun loadIcons(context: Context, style: Style) {
        val icons = PoiHelper().icons
        icons.forEach { icon ->
            val bitmap =
                BitmapFactory.decodeResource(context.resources, getResourceId(context, icon.path))
            val name = icon.path.split('.').last()
            style.addImage("${name}_icon", bitmap)
        }
    }

    fun matchIconToName(): Expression {
        return match {
            get { literal("icon") } // get the icon property from the GeoJSON data
            PoiHelper().icons.forEach { icon ->
                literal(icon.path.split('.').last()) // match the icon property
                literal(
                    "${
                        icon.path.split('.').last()
                    }_icon"
                ) // use the corresponding icon
            }
            literal("office") // use a default icon if there's no match
        }
    }

    fun calculateDistance(
        latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double
    ): Double {
        val firstPoint = Point.fromLngLat(longitude1, latitude1)
        val secondPoint = Point.fromLngLat(longitude2, latitude2)

        return TurfMeasurement.distance(firstPoint, secondPoint, TurfConstants.UNIT_METERS)
    }

    fun modifyNumberStyle(
        text: String, textToModify: List<String>, context: Context
    ): SpannableString {
        val spannable = SpannableString(text)
        for (i in textToModify.indices) {
            val start = text.indexOf(textToModify[i]) // find the number
            val end = start + textToModify[i].length
            spannable.setSpan(
                TypefaceSpan(ResourcesCompat.getFont(context, R.font.inter_extrabold)!!),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.secondary_colour)),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannable
    }

    data class Properties(
        val url: String,
        val poiName: String,
        val scenario: Int,
        val task: Int,
        val adaptive: Boolean
    ) : Serializable

    fun changeRatingBarStyling(view: View, linearLayout: LinearLayout, context: Context) {
        //Change the styling of the button clicked
        view.setBackgroundResource(R.drawable.box_filled)
        (view as Button).setTextColor(ContextCompat.getColor(context, R.color.white))

        //Change styling of all other buttons
        for (i in 0 until linearLayout.childCount) {
            val button = linearLayout.getChildAt(i) as Button
            if (button != view) {
                button.setBackgroundResource(R.drawable.box_empty)
                button.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    fun checkMovementSpeed(
        speed: Float,
        speedWarningPanel: LinearLayout,
        confirmationBtn: ImageButton,
        threshold: Double,
        mapView: MapView,
        locationIcon: FloatingActionButton,
        context: Context
    ) {

        if (speed <= threshold && speedWarningPanel.visibility == View.GONE) {
            // display warning
            speedWarningPanel.visibility = View.VISIBLE
            confirmationBtn.visibility = View.VISIBLE
            mapView.visibility = View.GONE
            locationIcon.visibility = View.GONE

            // Record the start time of the warning message display
            SharedPreferencesHelper(context).updateWarningMsgStartTime(System.currentTimeMillis())
        }
        if (speed > threshold && speedWarningPanel.visibility == View.VISIBLE) {
            // hide warning
            speedWarningPanel.visibility = View.GONE
            confirmationBtn.visibility = View.GONE
            mapView.visibility = View.VISIBLE
            locationIcon.visibility = View.VISIBLE

            // Record the end time of the warning message display
            SharedPreferencesHelper(context).updateWarningMsgEndTime(System.currentTimeMillis())
        }

    }

}

