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
    private val locationPermissionCode = 2
    lateinit var loadingView: ImageView
    lateinit var bannerView: ImageView

    val commonPestDict = mutableMapOf("Alabama" to listOf<String>("cockroach","ants","snakes"),
         "Alaska" to listOf<String>("mosquito","wasp","ant"),
         "Arizona" to listOf<String>("scorpion"),
        "Arkansas" to listOf<String>("spider"),
        "California" to listOf<String>("mouse" ,"mosquito","spider","wasp","cockroach"),
        "Colorado" to listOf<String>("mouse","wasp","sider"),
        "Connecticut" to listOf<String>("mouse","ant"),
        "District of Colombia" to listOf<String>("bee"),
        "Delaware" to listOf<String>("cockroach"),
        "Florida" to listOf<String>("cockroach","ant","mosquito"),
        "Georgia" to listOf<String>("cockroach","ant","bee"),
        "Hawaii" to listOf<String>("cockroach"),
        "Idaho" to listOf<String>("spider","ant"),
        "Illinois" to listOf<String>("bee","beetle"),
        "Indiana" to listOf<String>("bee"),
        "Iowa" to listOf<String>("mouse","spider"),
        "Kansas" to listOf<String>("bee","spider"),
        "Kentucky" to listOf<String>("bee"),
        "Louisiana" to listOf<String>("cockroach","ant"),
        "Maine" to listOf<String>("mouse","ant"),
        "Maryland" to listOf<String>("mouse","cockroach"),
        "Massachusetts" to listOf<String>("mouse","bee","ant"),
        "Michigan" to listOf<String>("ant"),
        "Minnesota" to listOf<String>("mouse"),
        "Mississippi" to listOf<String>("ant","snake"),
        "Missouri" to listOf<String>("bee"),
        "Montana" to listOf<String>("ant","bee","spdier"),
        "Nebraska" to listOf<String>("beetle"),
        "Nevada" to listOf<String>("scorpion","ant"),
        "NewHampshire" to listOf<String>("mouse","ant"),
        "NewJersey" to listOf<String>("mouse","wasp","beetle"),
        "NewMexico" to listOf<String>("ant"),
        "NewYork" to listOf<String>("mouse","cockroach"),
        "NorthCarolina" to listOf<String>("cockroach"),
        "NorthDakota" to listOf<String>("mouse","mosquito","snail"),
        "Ohio" to listOf<String>("bee","mosquito","wasp","ant","cockroach"),
        "Oklahoma" to listOf<String>("spider"),
        "Oregon" to listOf<String>("mouse","bee"),
        "Pennsylvania" to listOf<String>("mouse","cockroach"),
        "RhodeIsland" to listOf<String>("mouse","mosquito","ant"),
        "SouthCarolina" to listOf<String>("ant"),
        "SouthDakota" to listOf<String>("mosquito","spider"),
        "Tennessee" to listOf<String>("bee"),
        "Texas" to listOf<String>("cockroach","ant","mosquito","ant","wasp","bee"),
        "Utah" to listOf<String>("spider","scorpion"),
        "Vermont" to listOf<String>("mouse","ant"),
        "Virginia" to listOf<String>("mouse","cockroach"),
        "Washington" to listOf<String>("spider"),
        "WestVirginia" to listOf<String>("snake"),
        "Wisconsin" to listOf<String>("mouse","beetle"),
        "Wyoming" to listOf<String>("spider"),
        )

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
                    tempString = tempString.substring(0,4)
                }
                tempText.text= "$tempString \u2109"
            } else{
                var tempString = temp.toString()
                if (tempString.length>4){
                    tempString = tempString.substring(0,4)
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
