package com.example.weather.ui.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.adapter.CityDailyAdapter
import com.example.weather.databinding.FragmentCityDailyBinding
import com.example.weather.utils.Constant
import com.example.weather.viewmodel.CityDailyViewModel
import im.delight.android.location.SimpleLocation
import kotlinx.android.synthetic.main.fragment_city_daily.*

class CityDailyFragment : Fragment() {

    private val requestCode = 1

    private var location: SimpleLocation? = null
    private var latitude: String? = null
    private var longitude: String? = null

    private lateinit var viewModel : CityDailyViewModel
    private lateinit var dataBinding: FragmentCityDailyBinding

    private var cityDailyAdapter = CityDailyAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater,
                R.layout.fragment_city_daily, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = cityDailyAdapter

        viewModel = ViewModelProviders.of(this).get(CityDailyViewModel::class.java)

        location = SimpleLocation(context)

        if (!location!!.hasLocationEnabled()) {
            SimpleLocation.openSettings(context)
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    requestCode
                )
            } else {
                location = SimpleLocation(context)
                latitude = String.format("%.6f", location?.latitude)
                longitude = String.format("%.6f", location?.longitude)
                Log.e("LAT1", "" + latitude)
                Log.e("LONG1", "" + longitude)

            }
        }

        viewModel.getCityDailyWeatherFromGps(latitude!!,longitude!!, Constant.CNT,Constant.METRIC)

        viewModel.cityDailyData.observe(viewLifecycleOwner, Observer {cityDailyWeatherGps ->
            cityDailyWeatherGps.let {
                recyclerView.visibility = View.VISIBLE
                cityDailyAdapter.updateCountryList(cityDailyWeatherGps)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                location = SimpleLocation(context)
                latitude = String.format("%.6f", location?.latitude)
                longitude = String.format("%.6f", location?.longitude)
                Log.e("LAT", "" + latitude)
                Log.e("LONG", "" + longitude)

                viewModel.getCityDailyWeatherFromGps(latitude!!,longitude!!,Constant.CNT,Constant.METRIC)

            } else {
                Toast.makeText(context, "City Daily Fragment", Toast.LENGTH_LONG)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}