package com.janlyczakowski.masterthesis

import android.content.Context
import android.content.Intent
import android.location.LocationListener
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.common.location.AccuracyLevel
import com.mapbox.common.location.DeviceLocationProvider
import com.mapbox.common.location.IntervalSettings
import com.mapbox.common.location.LocationProviderRequest
import com.mapbox.common.location.LocationService
import com.mapbox.common.location.LocationServiceFactory
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraBoundsOptions
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.QueriedRenderedFeature
import com.mapbox.maps.RenderedQueryGeometry
import com.mapbox.maps.RenderedQueryOptions
import com.mapbox.maps.extension.style.expressions.dsl.generated.match
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.SymbolLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.getLayerAs
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MapActivity : AppCompatActivity() {

    private lateinit var geojson: JSONObject
    private lateinit var mapScenario: Utils.Properties
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var locationManager: LocationManager
    private var speedWarningDisabled = false


    // Location Service

    private val locationService: LocationService = LocationServiceFactory.getOrCreate()
    private var locationProvider: DeviceLocationProvider? = null

    private val request = LocationProviderRequest.Builder()
        .interval(
            IntervalSettings.Builder().interval(0L).minimumInterval(0L).maximumInterval(0L).build()
        )
        .displacement(0F)
        .accuracy(AccuracyLevel.HIGHEST)
        .build()

    private val result = locationService.getDeviceLocationProvider(request)

    // Permission handler
    private lateinit var locationPermissionHelper: LocationPermissionHelper

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.mapboxMap.setCamera(CameraOptions.Builder().bearing(it).build())

    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView.mapboxMap.setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.mapboxMap.pixelForCoordinate(it)
    }

    // Dismisses the camera tracking when the user moves the map
    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }

    private lateinit var mapView: MapView
    private lateinit var loadingPanel: CardView
    private lateinit var successMessage: LinearLayout
    private lateinit var locationIcon: FloatingActionButton
    private lateinit var speedWarningPanel: LinearLayout
    private lateinit var confirmationBtn: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingMessage: TextView
    private lateinit var failureMessage: TextView

    private var totalWarningDuration: Long = 0
    private var warningDisabledTime: Long = 0
    private var dataLoaded: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapScenario = intent.getSerializableExtra("properties") as Utils.Properties

        // Initialize the LocationManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        setContentView(R.layout.map_activity)
        mapView = findViewById(R.id.mapView)
        loadingPanel = findViewById(R.id.loadingPanel)
        locationIcon = findViewById(R.id.locationIcon)
        successMessage = findViewById(R.id.successPanel)
        speedWarningPanel = findViewById(R.id.speedWarningPanel)
        confirmationBtn = findViewById(R.id.confirmationBtn)

        // reset the warning message start and end time everytime the activity is created
        SharedPreferencesHelper(this).updateWarningMsgStartTime(0)
        SharedPreferencesHelper(this).updateWarningMsgEndTime(0)

        // Highlight the words in the warning message
        val text1 = getString(R.string.speed_warning_message_2)
        val textToModify1 = listOf("speed up")
        val text1Modified = Utils().modifyNumberStyle(text1, textToModify1, this)
        val text1View = findViewById<TextView>(R.id.speed_warning_message_2)
        text1View.text = text1Modified

        val text2 = getString(R.string.speed_warning_message_4)
        val textToModify2 = listOf("green button")
        val text2Modified = Utils().modifyNumberStyle(text2, textToModify2, this)
        val text2View = findViewById<TextView>(R.id.speed_warning_message_4)
        text2View.text = text2Modified



        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }
    }

    private suspend fun findClosestPoi(
        clickedFeature: QueriedRenderedFeature,
        function: String
    ): Boolean {
        var correctAnswer = false
        var closestFeatureDistance: Double? = null
        var closestFeatureId: Int? = null
        if (clickedFeature.queriedFeature.feature.getStringProperty("function") == function) {
            // take all of the points with passed function
            val features = geojson.getJSONArray("features")
            for (i in 0 until features.length()) {
                val feature = features.getJSONObject(i)
                val properties = feature.getJSONObject("properties")
                if (properties.getString("function") == function) {

                    val coordinates = getLatestLocation()
                    val longitude = coordinates[0]
                    val latitude = coordinates[1]

                    // calculate the distance between the clicked point and all of the points with the same function
                    val distance = utils.calculateDistance(
                        properties.getDouble("latitude"),
                        properties.getDouble("longitude"),
                        latitude,
                        longitude
                    )
                    if ((closestFeatureDistance == null) || (distance < closestFeatureDistance)) {
                        closestFeatureDistance = distance
                        closestFeatureId = feature.getInt("id")

                    }

                }
            }

        }

        if (clickedFeature.queriedFeature.feature.getNumberProperty("id")
                .toInt() == closestFeatureId
        ) {
            correctAnswer = true

        }
        return correctAnswer
    }

    private val utils = Utils()
    private fun onMapReady() {
        var startTaskTime: Long = 0
        loadingPanel.visibility = ProgressBar.VISIBLE

        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .zoom(15.0)
                .bearing(0.0)
                .pitch(0.0)
                .build()

        )


        mapView.mapboxMap.loadStyle(
            "mapbox://styles/lyczakowskijan/clwro471x00xd01pc2uij4lzj"
        ) { style ->
            initLocationComponent()
            setupGesturesListener()

            // set zoom restrictions
            setupBounds(CAMERA_RESTRICTIONS)

            // add images to the style
            utils.loadIcons(this, style)

            // fetch geojson data

            val dataHelper = FetchDataHelper()
            dataHelper.GET(mapScenario.url, object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    runOnUiThread {
                        try {
                            val json = JSONObject(responseData)
                            println("Successful response")
                            geojson = dataHelper.convertToGeoJSON(json)
                            val source = geoJsonSource(Constants.SOURCE_ID) {
                                data(geojson.toString())
                            }

                            style.addSource(source)
                            loadingPanel.visibility = ProgressBar.GONE
                            dataLoaded = true

                            startTaskTime = System.currentTimeMillis() // start the timer

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    println("Request Failure.")
                    progressBar.visibility = ProgressBar.GONE
                    loadingMessage.visibility = TextView.GONE
                    failureMessage.visibility = TextView.VISIBLE
                }
            })

            // Create a symbol layer and access the layer contained.
            style.addLayer(symbolLayer(Constants.LAYER_ID, Constants.SOURCE_ID) {
                sourceLayer(Constants.SOURCE_LAYER_ID)

                iconImage(utils.matchIconToName())
                iconSize(0.9)
                iconAllowOverlap(true)
                iconAnchor(IconAnchor.CENTER)
            })

        }

        mapView.mapboxMap.addOnMapClickListener { point ->
            val pixel = mapView.mapboxMap.pixelForCoordinate(point)
            val queryGeometry = RenderedQueryGeometry(pixel)
            val options = RenderedQueryOptions(null, null)


            mapView.mapboxMap.queryRenderedFeatures(
                queryGeometry, options
            ) { features ->

                if (features.value?.isNotEmpty() == true) {


                    val clickedFeature = features.value!![0]

                    if (clickedFeature.queriedFeature.feature.getStringProperty("function") != null && clickedFeature.queriedFeature.feature.id() != null) {

                        speedWarningDisabled = true
                        // change the opacity of the features that were not clicked
                        mapView.mapboxMap.getStyle { style ->
                            style.getLayerAs<SymbolLayer>(Constants.LAYER_ID)?.iconOpacity(
                                match {
                                    get { literal("id") }
                                    literal(clickedFeature.queriedFeature.feature.id()!!)
                                    literal(1.0)
                                    literal(0.0)
                                }

                            )
                        }

                        // Stop the timer and calculate the time
                        val timeSeconds =
                            ((System.currentTimeMillis() - startTaskTime) / 1000) - totalWarningDuration


                        // look for the closest from feature from current user's location with particular function
                        lifecycleScope.launch {
                            val isAnswerCorrect =
                                findClosestPoi(clickedFeature, mapScenario.poiName)


                            // save (time and correctness) in shared preferences
                            val sharedPreferencesHelper = SharedPreferencesHelper(this@MapActivity)
                            if (mapScenario.adaptive) {
                                sharedPreferencesHelper.updateMapDataA(
                                    timeSeconds.toFloat(),
                                    if (isAnswerCorrect) 1 else 0
                                )
                            } else {
                                sharedPreferencesHelper.updateMapDataNA(
                                    timeSeconds.toFloat(),
                                    if (isAnswerCorrect) 1 else 0
                                )
                            }

                        }
                        // Display the success message after 1 with smooth transition

                        mapView.animate().alpha(0f).setDuration(500).withEndAction {
                            mapView.visibility = View.GONE
                        }
                        locationIcon.animate().alpha(0f).setDuration(500).withEndAction {
                            locationIcon.visibility = View.GONE
                        }
                        successMessage.apply {
                            alpha = 0f
                            visibility = View.VISIBLE
                            animate().alpha(1f).setDuration(500).startDelay = 500
                        }

                        // Start another activity after 2 s

                        Handler(Looper.getMainLooper()).postDelayed({
                            // if that was the I task of scenario go to the confidence test
                            // if that was the II task of scenario go to the post test survey
                            val properties =
                                Utils.Properties(
                                    mapScenario.url,
                                    mapScenario.poiName,
                                    mapScenario.scenario,
                                    mapScenario.task,
                                    mapScenario.adaptive
                                )

                            val intent = Intent(this, ConfidenceTest::class.java)
                            intent.putExtra("properties", properties)
                            startActivity(intent)

                        }, 2000)

                    } else (
                            Toast.makeText(
                                this@MapActivity,
                                "Click on the icon",
                                Toast.LENGTH_SHORT
                            ).show()
                            )

                }
            }
            return@addOnMapClickListener false
        }
    }

    private suspend fun getLatestLocation(): List<Double> = suspendCoroutine { continuation ->
        var latestLon: Double
        var latestLat: Double
        if (result.isValue) {
            locationProvider = result.value!!
            locationProvider!!.getLastLocation { result ->
                result?.let {
                    latestLon = it.longitude
                    latestLat = it.latitude
                    continuation.resume(listOf(latestLon, latestLat))

                }
            }
        } else {

            Toast.makeText(
                applicationContext,
                "Failed to get device location location",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun onClickLocateMeListener(View: View) {

        locationPermissionHelper.checkPermissions {
            lifecycleScope.launch {
                val coordinates = getLatestLocation()
                val longitude = coordinates[0]
                val latitude = coordinates[1]
                mapView.mapboxMap.setCamera(
                    CameraOptions.Builder().center(Point.fromLngLat(longitude, latitude)).zoom(18.0)
                        .build()
                )
            }
        }
    }

    private fun setupGesturesListener() {
        mapView.gestures.addOnMoveListener(onMoveListener)
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            puckBearing = PuckBearing.COURSE
            puckBearingEnabled = true
            enabled = true
            locationPuck = createDefault2DPuck(withBearing = true)
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(
            onIndicatorPositionChangedListener
        )
        locationComponentPlugin.addOnIndicatorBearingChangedListener(
            onIndicatorBearingChangedListener
        )
    }

    private fun onCameraTrackingDismissed() {
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setupBounds(bounds: CameraBoundsOptions) {
        mapView.mapboxMap.setBounds(bounds)
    }

    companion object {
        private val CAMERA_RESTRICTIONS: CameraBoundsOptions = CameraBoundsOptions.Builder()
            .minZoom(16.0)
            .build()
    }

    override fun onResume() {
        super.onResume()

        // Register the LocationListener
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            locationListener
        )


        // play ambient sound for the particular scenario
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        when (mapScenario.scenario) {
            1 -> {
                mediaPlayer = MediaPlayer.create(this, R.raw.busy_street)
                mediaPlayer.isLooping = true // play sound all the time

                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    (maxVolume / 3),
                    0
                )

                mediaPlayer.start()
            }

            2 -> {
                mediaPlayer = MediaPlayer.create(this, R.raw.city_street)
                mediaPlayer.isLooping = true

                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    (maxVolume / 4),
                    0
                )

                mediaPlayer.start()
            }

            3 -> {
                mediaPlayer = MediaPlayer.create(this, R.raw.city_park)
                mediaPlayer.isLooping = true // play sound all the time

                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    (maxVolume / 4),
                    0
                )

                mediaPlayer.start()
            }
        }
        // Direct the volume controls to a stream that is not currently being used
        // Alarm stream chosen so the user can't control the sound of the music played
        volumeControlStream = AudioManager.STREAM_ALARM
    }

    override fun onPause() {
        super.onPause()
        // Stop the media player when the activity is no longer in the foreground
        mediaPlayer.stop()
        mediaPlayer.release()

        // Reset the volume controls to the default stream
        volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE

        // Unregister the LocationListener
        locationManager.removeUpdates(locationListener)

    }

    // Disable the UI when the user presses the volume up or down keys
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val action = event.action
        val keyCode = event.keyCode
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP, KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (action == KeyEvent.ACTION_DOWN) {
                    // If the volume up or down keys are pressed, consume the event and do nothing
                    true
                } else {
                    super.dispatchKeyEvent(event)
                }
            }

            else -> super.dispatchKeyEvent(event)
        }
    }

    fun onConfirmationBtnClickListener(view: View) {
        // turn off the warning panel
        speedWarningPanel.visibility = View.GONE
        confirmationBtn.visibility = View.GONE
        mapView.visibility = View.VISIBLE
        locationIcon.visibility = View.VISIBLE

        // disable warning panel from showing again
        speedWarningDisabled = true

        // start measure time after the warning was disabled
        warningDisabledTime = System.currentTimeMillis()

        // Record the end time of the warning message display
        val warningEndTime = System.currentTimeMillis()
        SharedPreferencesHelper(this).updateWarningMsgEndTime(warningEndTime)
        val startWarningTime = SharedPreferencesHelper(this).getWarningMsgStartTime()
        // Calculate the duration of the warning message display and add it to the total warning duration
        totalWarningDuration += (warningEndTime - startWarningTime) / 1000
        // Reset the start time of the warning message display
        SharedPreferencesHelper(this).updateWarningMsgStartTime(0)
        SharedPreferencesHelper(this).updateWarningMsgEndTime(0)
    }

    private val locationListener: LocationListener = LocationListener { location ->
        // Get the speed from the location
        val speed = location.speed // speed in meter/second

        // Give the user 5s to complete the task after the warning message was disabled
        if (warningDisabledTime.toInt() != 0 && System.currentTimeMillis() - warningDisabledTime > 5000) {
            speedWarningDisabled = false
        }
        // Display the warning message only when the geojson is loaded
        if (!speedWarningDisabled && dataLoaded) {
            if (mapScenario.scenario == 1 || mapScenario.scenario == 3) {
                Utils().checkMovementSpeed(
                    speed,
                    speedWarningPanel,
                    confirmationBtn,
                    Constants.LOW_SPEED_THRESHOLD, mapView, locationIcon, this
                )

                val startWarningTime = SharedPreferencesHelper(this).getWarningMsgStartTime()
                val endWarningTime = SharedPreferencesHelper(this).getWarningMsgEndTime()

                if (startWarningTime.toInt() != 0 && endWarningTime.toInt() != 0) {
                    totalWarningDuration += (endWarningTime - startWarningTime) / 1000

                    // Reset the start time of the warning message display
                    SharedPreferencesHelper(this).updateWarningMsgStartTime(0)
                    SharedPreferencesHelper(this).updateWarningMsgEndTime(0)
                }


            } else if (mapScenario.scenario == 2) {
                Utils().checkMovementSpeed(
                    speed,
                    speedWarningPanel,
                    confirmationBtn,
                    Constants.HIGH_SPEED_THRESHOLD, mapView, locationIcon, this
                )
                val startWarningTime = SharedPreferencesHelper(this).getWarningMsgStartTime()
                val endWarningTime = SharedPreferencesHelper(this).getWarningMsgEndTime()

                if (startWarningTime.toInt() != 0 && endWarningTime.toInt() != 0) {
                    totalWarningDuration += (endWarningTime - startWarningTime) / 1000

                    // Reset the start time of the warning message display
                    SharedPreferencesHelper(this).updateWarningMsgStartTime(0)
                    SharedPreferencesHelper(this).updateWarningMsgEndTime(0)
                }


            }
        }

    }
}
