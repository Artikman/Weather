package com.example.weather

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.adapter.HourlyAdapter
import com.example.weather.utils.Constant
import com.example.weather.viewmodel.FiveDaysViewModel
import im.delight.android.location.SimpleLocation
import kotlinx.android.synthetic.main.fragment_five_days.*

class FiveDaysFragment : Fragment() {

    private val requestCode = 1

    private var location: SimpleLocation? = null
    private var latitude: String? = null
    private var longitude: String? = null

    private lateinit var viewModel: FiveDaysViewModel
    private val hourlyAdapter = HourlyAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_five_days, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProviders.of(this).get(FiveDaysViewModel::class.java)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = hourlyAdapter

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
        viewModel.getForecastFromGps(latitude!!, longitude!!, Constant.METRIC)

        viewModel.forecastData.observe(viewLifecycleOwner, Observer { forecastGps ->
            forecastGps?.let {
                crdFiveDays.visibility = View.VISIBLE
                hourlyAdapter.updateHourlyList(forecastGps)
            }
        })

        viewModel.fiveDaysLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if(it){
                    fiveDaysLoading.visibility = View.VISIBLE
                    crdFiveDays.visibility = View.GONE
                }else{
                    fiveDaysLoading.visibility = View.GONE
                }
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

                viewModel.getForecastFromGps(latitude!!, longitude!!, Constant.METRIC)

            } else {
                Toast.makeText(context, "Five Days Fragment", Toast.LENGTH_LONG)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}