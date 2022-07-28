package com.dotexe.homework3

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONException


class WeatherFragment : Fragment(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var tempText: TextView
    private lateinit var cityText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var suggestionText: TextView
    private val locationPermissionCode = 2
    lateinit var loadingView: ImageView
    lateinit var bannerView: ImageView

    val commonPestDict = mutableMapOf("AL" to listOf<String>("cockroach","ants","snakes"),
         "AK" to listOf<String>("mosquito","wasp","ant"),
         "AZ" to listOf<String>("scorpion"),
        "AR" to listOf<String>("spider"),
        "CA" to listOf<String>("mouse" ,"mosquito","spider","wasp","cockroach"),
        "CO" to listOf<String>("mouse","wasp","sider"),
        "CT" to listOf<String>("mouse","ant"),
        "DC" to listOf<String>("bee"),
        "DE" to listOf<String>("cockroach"),
        "FL" to listOf<String>("cockroach","ant","mosquito"),
        "GA" to listOf<String>("cockroach","ant","bee"),
        "HI" to listOf<String>("cockroach"),
        "ID" to listOf<String>("spider","ant"),
        "IL" to listOf<String>("bee","beetle"),
        "IN" to listOf<String>("bee"),
        "IA" to listOf<String>("mouse","spider"),
        "KS" to listOf<String>("bee","spider"),
        "KY" to listOf<String>("bee"),
        "LA" to listOf<String>("cockroach","ant"),
        "ME" to listOf<String>("mouse","ant"),
        "MD" to listOf<String>("mouse","cockroach"),
        "MA" to listOf<String>("mouse","bee","ant"),
        "MI" to listOf<String>("ant"),
        "MN" to listOf<String>("mouse"),
        "MS" to listOf<String>("ant","snake"),
        "MO" to listOf<String>("bee"),
        "MT" to listOf<String>("ant","bee","spdier"),
        "NE" to listOf<String>("beetle"),
        "NV" to listOf<String>("scorpion","ant"),
        "NH" to listOf<String>("mouse","ant"),
        "NJ" to listOf<String>("mouse","wasp","beetle"),
        "NM" to listOf<String>("ant"),
        "NY" to listOf<String>("mouse","cockroach"),
        "NC" to listOf<String>("cockroach"),
        "ND" to listOf<String>("mouse","mosquito","snail"),
        "OH" to listOf<String>("bee","mosquito","wasp","ant","cockroach"),
        "OK" to listOf<String>("spider"),
        "OR" to listOf<String>("mouse","bee"),
        "PA" to listOf<String>("mouse","cockroach"),
        "RI" to listOf<String>("mouse","mosquito","ant"),
        "SC" to listOf<String>("ant"),
        "SD" to listOf<String>("mosquito","spider"),
        "TN" to listOf<String>("bee"),
        "TX" to listOf<String>("cockroach","ant","mosquito","ant","wasp","bee"),
        "UT" to listOf<String>("spider","scorpion"),
        "VT" to listOf<String>("mouse","ant"),
        "VA" to listOf<String>("mouse","cockroach"),
        "WA" to listOf<String>("spider"),
        "WV" to listOf<String>("snake"),
        "WI" to listOf<String>("mouse","beetle"),
        "WY" to listOf<String>("spider"),
        )

    //api id for url
    var api_id1 = "eb1fbbe5a02240e7a29493eede818b43"
    private var requestQueue: RequestQueue? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_weather, container, false)
        val returnToMain = view.findViewById<ImageButton>(R.id.goToMain)
        returnToMain.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_weatherFragment_to_menuFragment)}

        tempText = view.findViewById<TextView>(R.id.temperature)
        cityText = view.findViewById<TextView>(R.id.city)
        descriptionText = view.findViewById<TextView>(R.id.description)
        loadingView = view.findViewById(R.id.loadingView)
        bannerView = view.findViewById(R.id.bottomBanner)
        suggestionText = view.findViewById(R.id.suggestion)

        //loading spinner gif on create
        Glide.with(view.context).load(R.drawable.spinner).into(loadingView)

        requestQueue = Volley.newRequestQueue(view.context)
        locationManager = view.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(view.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
            return view
    }
    override fun onLocationChanged(location: Location) {
        getTemperature(location.latitude, location.longitude)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun getSuggestion(statecode: String) {
        var message = "Based on your location, we recommend selecting to repel the "
        for ((state, list) in commonPestDict) {
            if (state == statecode) {
                for (pest in list) {
                    if (list.indexOf(pest) == list.size-1){
                        message = message.plus(" and ").plus(pest).plus(".")
                    } else {
                        message = message.plus(pest).plus(", ")
                    }
                    Log.e("message", message)
                }
            }
        }
        suggestionText.text =  message


    }
    fun getTemperature( latitude: Double, longitude: Double) {
        //https://riis.com/blog/sending-requests-using-android-volley/
        val url = "https://api.weatherbit.io/v2.0/current?" + "lat=" + latitude +"&lon="+ longitude + "&key="+ api_id1
        Log.e("lat", url)
        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {
                    val jsonArray = response.getJSONArray("data")
            Log.e("lat","parsing")
                    val info = jsonArray.getJSONObject(0)
                    var temp = info.getDouble("temp")
                    val cityname = info.getString("city_name")
                    val statecode = info.getString("state_code")
                    val countrycode = info.getString("country_code")
                    val description = info.getJSONObject("weather").getString("description")
            //removing loading gif from view
            loadingView.setImageDrawable(null)
            //setting banner into view
            bannerView.setImageResource(R.drawable.picnic)
            //setting weather stuff into view
            if (countrycode.equals("US")){
                var tempString = (temp*(1.8) + 32).toString()
                if (tempString.length>4){
                    tempString = tempString.substring(0,4) }
                getSuggestion(statecode)
                tempText.text= "$tempString\u2109"
                } else{
                    var tempString = temp.toString()
                    if (tempString.length>4){
                    tempString = tempString.substring(0,4) }
                tempText.text= "$tempString\u2103"
                suggestionText.text =  "We are sorry. our recommendation service does not work in your country."
            }
            cityText.text = "$cityname, $statecode, $countrycode"
            descriptionText.text = "$description"
        } catch (e: JSONException) {
            e.printStackTrace() }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)
    }
}
