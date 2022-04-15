package com.example.homework3

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation

class WeatherFragment : Fragment(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private val locationPermissionCode = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_weather, container, false)


        //link the textView in which the temperature will be displayed
        val button: Button = view.findViewById(R.id.getLocation)
        button.setOnClickListener {
            //locationManager = getSystemService(context.LOCATION_SERVICE) as LocationManager
            locationManager = view.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if ((ContextCompat.checkSelfPermission(view.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }

            return view
    }


    override fun onLocationChanged(location: Location) {
        tvGpsLocation = requireView().findViewById<TextView>(R.id.textView)
        tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
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
}

/*
    //weather url to get JSON
    var weather_url1 = ""
    //api id for url
    var api_id1 = "eb1fbbe5a02240e7a29493eede818b43"
    private lateinit var textView: TextView
    private lateinit var refreshButton: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_weather, container, false)

        //link the textView in which the temperature will be displayed
        textView = view.findViewById<TextView>(R.id.textView)
        refreshButton = view.findViewById<Button>(R.id.refresh)
        //create an instance of the Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        //Log.e("lat", weather_url1)
        //on clicking this button function to get the coordinates will be called
        refreshButton.setOnClickListener {
            //Log.e("lat", "onClick")
            //textView.text = "updating now"
            //function to find the coordinates of the last location
            obtainLocation()
            getTemperature()
        }

        // Inflate the layout for this fragment
        return view


    }
    @SuppressLint("MissingPermission")
    private fun obtainLocation(){
        //Log.e("lat", "function")
        //get the last location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                //get the latitute and longitude and create the http URL
               //weather_url1 = "https://api.weatherbit.io/v2.0/current?" + "lat=" + location?.latitude +"&lon="+ location?.longitude + "&key="+ api_id1
                //weather_url1 = "https://api.weatherbit.io/v2.0/current?lat=35.7796&lon=-78.6382&key=API_KEY&include=minutely"

                textView.text = "updating now"
                //textView.text = "$location"
                //Log.e("lat", weather_url1.toString())
                //this function will fetch data from URL
                getTemperature() //make a request form this URL to get data
            }
    }

    fun getTemperature() {
        //https://riis.com/blog/sending-requests-using-android-volley/

        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(activity?.applicationContext)
        weather_url1 = "https://api.weatherbit.io/v2.0/current?" + "lat=" + "35.7796" +"&lon="+ "-78.6382" + "&key="+ api_id1

        val url: String = weather_url1
        //Log.e("lat", url)
        // Request a string response
        // from the provided URL.
        val stringReq = StringRequest(Request.Method.GET, url, Response.Listener
            { response ->
                //Log.e("lat", response.toString())

                // get the JSON object
                val obj = JSONObject(response)
                textView.text = obj.toString()
                // get the Array from obj of name - "data"
                //val arr = obj.getJSONArray("data")
                //Log.e("lat obj1", arr.toString())

                // get the JSON object from the
                // array at index position 0
                //val obj2 = arr.getJSONObject(0)
                //Log.e("lat obj2", obj2.toString())

                // set the temperature and the city
                // name using getString() function
                //textView.text = obj2.getString("temp") + " deg Celsius in " + obj2.getString("city_name")
                //textView.text = obj2.getString("temp")
            },
            // In case of any error
           { textView.text = "That didn't work!" }
        )
        queue.add(stringReq)
    }
}
*/