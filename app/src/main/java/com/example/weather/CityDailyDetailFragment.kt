package com.example.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.weather.databinding.FragmentOneDayDetailBinding
import com.example.weather.model.CityDailyResponse

class CityDailyDetailFragment : Fragment() {

    private lateinit var dataBinding: FragmentOneDayDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_one_day_detail, container, false)
        val cityDailyResponse = arguments?.getParcelable<CityDailyResponse.Forecast>("cityWeatherDetail")
        dataBinding.detail = cityDailyResponse
        return dataBinding.root
    }
}