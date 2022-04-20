package com.example.homework3

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
    private val locationPermissionCode = 2
    lateinit var loadingView: ImageView
    lateinit var bannerView: ImageView

    //weather url to get JSON
    var weather_url1 = ""
    //api id for url
    var api_id1 = "eb1fbbe5a02240e7a29493eede818b43"
    private var requestQueue: RequestQueue? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_weather, container, false)
        val returnToMain = view.findViewById<ImageButton>(R.id.returnToMain)
        returnToMain.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_weatherFragment_to_menuFragment)}

        tempText = view.findViewById<TextView>(R.id.temperature)
        cityText = view.findViewById<TextView>(R.id.city)
        descriptionText = view.findViewById<TextView>(R.id.description)
        loadingView = view.findViewById(R.id.imageView)
        bannerView = view.findViewById(R.id.bottomBanner)

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
        //tempText.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
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
    fun getTemperature( latitude: Double, longitude: Double) {
        //https://riis.com/blog/sending-requests-using-android-volley/
        val url = "https://api.weatherbit.io/v2.0/current?" + "lat=" + latitude +"&lon="+ longitude + "&key="+ api_id1
        Log.e("lat", url)
        val request = JsonObjectRequest(Request.Method.GET, url, null, {
                response ->try {
                    val jsonArray = response.getJSONArray("data")

                    val info = jsonArray.getJSONObject(0)
            Log.e("lat", info.toString())
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
                    tempString = tempString.substring(0,3)
                }
                tempText.text= "$tempString \u2109"
            } else{
                var tempString = temp.toString()
                if (tempString.length>4){
                    tempString = tempString.substring(0,3)
                }
                tempText.text= "$tempString \u2103"
            }
            cityText.text = "$cityname, $statecode, $countrycode"
            descriptionText.text = "$description"

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)
    }

}
