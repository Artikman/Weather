package com.example.weather.ui.fragments


import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.weather.R
import com.example.weather.databinding.FragmentLocationBinding
import com.example.weather.utils.Constant
import com.example.weather.utils.dateConverter
import com.example.weather.utils.timeConverter
import com.example.weather.viewmodel.LocationViewModel
import im.delight.android.location.SimpleLocation
import kotlinx.android.synthetic.main.fragment_location.*

class LocationFragment : Fragment() {

    private val REQUEST_CODE = 1

    private lateinit var viewModel: LocationViewModel
    private lateinit var dataBinding: FragmentLocationBinding

    var location: SimpleLocation? = null
    var latitude: String? = null
    var longitude: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_location, container, false
            )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)

        location = SimpleLocation(context)
        if (!location!!.hasLocationEnabled()) {
            SimpleLocation.openSettings(context)
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE
                )
            } else {
                location = SimpleLocation(context)
                latitude = String.format("%.6f", location?.latitude)
                longitude = String.format("%.6f", location?.longitude)
                Log.e("LAT1", "" + latitude)
                Log.e("LONG1", "" + longitude)
                viewModel!!.getWeatherDataWithGPS(latitude, longitude, Constant.METRIC)
            }
        }
        viewModel!!.locationData.observe(viewLifecycleOwner, Observer { locationGps ->
            locationGps?.let {
                lytLocation.visibility = View.VISIBLE
                dataBinding.locationGPS = locationGps
                dataBinding.tvTemperature.text = locationGps.main!!.temp.toInt().toString()
                dataBinding.tvDate.text = dateConverter()
                dataBinding.tvSunrise.text = timeConverter((locationGps.sys!!.sunrise).toLong())
                dataBinding.tvSunset.text = timeConverter((locationGps.sys!!.sunset).toLong())
                dataBinding.imgState.setImageResource(
                    resources.getIdentifier(
                        "ic_" + locationGps.weather?.get(
                            0
                        )?.icon, "drawable", view.context.packageName
                    )
                )
            }
        })

        viewModel!!.locationLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    locationLoading.visibility = View.VISIBLE
                    lytLocation.visibility = View.GONE
                } else {
                    locationLoading.visibility = View.GONE
                }
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                location = SimpleLocation(context)
                latitude = String.format("%.6f", location?.latitude)
                longitude = String.format("%.6f", location?.longitude)
                Log.e("LAT", "" + latitude)
                Log.e("LONG", "" + longitude)

                viewModel!!.getWeatherDataWithGPS(latitude!!, longitude!!, Constant.METRIC)

            } else {
                Toast.makeText(context, "İzin vereydin de konumunu bulaydık :P", Toast.LENGTH_LONG)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}