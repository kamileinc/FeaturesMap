package com.example.meridia_interview_task.ui.maps

import android.graphics.Color
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.meridia_interview_task.R
import com.example.meridia_interview_task.data.entity.Feature
import com.example.meridia_interview_task.data.network.FeatureApiService
import com.example.meridia_interview_task.utilities.FeatureStatus
import com.example.meridia_interview_task.utilities.Loading
import com.example.meridia_interview_task.utilities.Success
import com.example.meridia_interview_task.viewmodel.MapsFragmentViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsFragment : Fragment() {

    private lateinit var viewModel: MapsFragmentViewModel
    private var features = mutableListOf<Feature>()
    private val callback = OnMapReadyCallback { googleMap ->
        features.forEach { feature ->
            var polygonOptions = PolygonOptions()
            feature.points.forEach { point ->
                val latLng = LatLng(point.latitude, point.longitude)
                polygonOptions.add(latLng)
                val markerOptions = styleMarker(latLng, point.accuracy)

                googleMap.addMarker(markerOptions)

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, CAMERA_ZOOM))
            }
            polygonOptions = stylePolygon(polygonOptions)
            googleMap.addPolygon(polygonOptions)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MapsFragmentViewModel::class.java)

        viewModel.featureStatus.observe(viewLifecycleOwner, { status ->
            handleNewStatus(status)
        })

    }
    private fun handleNewStatus(status: FeatureStatus?) {
        status?.let {
            when (it) {
                is Success<*> -> handleSuccess(it)
                is Loading -> showLoading()
                else -> showError()
            }
        }
    }

    private fun showLoading() {
        Toast.makeText(requireActivity(), resources.getString(R.string.loading), Toast.LENGTH_SHORT)
            .show()
    }

     private fun showError() {
         Toast.makeText(requireActivity(), resources.getString(R.string.error), Toast.LENGTH_LONG)
             .show()
     }

    private fun handleSuccess(result: Success<*>) {
        features.addAll(result.data as List<Feature>)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        Toast.makeText(requireActivity(), resources.getString(R.string.loaded), Toast.LENGTH_LONG)
            .show()
    }
    private fun styleMarker(latLng: LatLng, accuracy: Double): MarkerOptions {
        var color = BitmapDescriptorFactory.HUE_GREEN
        var accuracyDescription = ""

        if (accuracy > 0.0 && accuracy <= 1.5) {
            color = BitmapDescriptorFactory.HUE_GREEN
            accuracyDescription = resources.getString(R.string.good_accuracy)
        } else if (accuracy > 1.5 && accuracy <= 2.0) {
            color = BitmapDescriptorFactory.HUE_YELLOW
            accuracyDescription = resources.getString(R.string.average_accuracy)
        } else if (accuracy > 2.0) {
            color = BitmapDescriptorFactory.HUE_RED
            accuracyDescription = resources.getString(R.string.poor_accuracy)
        }

        return MarkerOptions()
            .position(latLng)
            .title(accuracyDescription)
            .icon(BitmapDescriptorFactory.defaultMarker(color))
    }
    private fun stylePolygon(polygonOptions: PolygonOptions): PolygonOptions {

        return polygonOptions
            .strokeColor(Color.BLACK)
            .fillColor(TRANSPARENT_RED)
            .strokeWidth(POLYGON_STROKE_WIDTH)
    }
    companion object{
        private const val TRANSPARENT_RED = 0x2FFF0000
        private const val POLYGON_STROKE_WIDTH = 1F
        private const val CAMERA_ZOOM = 11F
    }
}

