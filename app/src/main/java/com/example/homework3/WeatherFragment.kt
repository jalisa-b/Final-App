package com.example.homework3

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import org.json.JSONException
import org.json.JSONObject

class WeatherFragment : Fragment(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private val locationPermissionCode = 2

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


        //link the textView in which the temperature will be displayed
        val button: Button = view.findViewById(R.id.getLocation)
        requestQueue = Volley.newRequestQueue(view.context)
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
                    val temp = info.getInt("temp")
                    val city = info.getString("timezone").filterNot { it == '/' }
                tvGpsLocation.text= "temperature: $temp city: $city"



        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)

    }
    /*
    fun getTemperature( latitude: Double, longitude: Double) {
        //https://riis.com/blog/sending-requests-using-android-volley/

        // Instantiate the RequestQueue.
        var queue = Volley.newRequestQueue(context)
        weather_url1 = "https://api.weatherbit.io/v2.0/current?" + "lat=" + latitude +"&lon="+ longitude + "&key="+ api_id1

        val url: String = weather_url1
        Log.e("lat", url)
        // Request a string response
        // from the provided URL.
        val stringReq = StringRequest(Request.Method.GET, url, Response.Listener<String>
            { response ->
                var strResp = response.toString()
                Log.e("lat", response.toString())

                // get the JSON object
                val obj = JSONObject(response)

                // get the Array from obj of name - "data"
                // val arr = obj.getJSONArray("data")
                //Log.e("lat obj1", arr.toString())

                // get the JSON object from the
                // array at index position 0
                //val obj2 = arr.getJSONObject(0)
                //Log.e("lat obj2", obj2.toString())

                // set the temperature and the city
                // name using getString() function
                //tvGpsLocation.text = obj2.getString("temp") + " deg Celsius in " + obj2.getString("city_name")
                tvGpsLocation.text = obj.toString()
            },
            // In case of any error
            { tvGpsLocation!!.text = "That didn't work!" })
        queue.add(stringReq)
    } */

}
