package com.tecsup.proyecto

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isRotateGesturesEnabled = false
        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle)
        )
        val marker = LatLng(-8.1323794,-79.0432113)
        map.addMarker(
            MarkerOptions()
                .position(marker)
                .title("Pets Friends")
        )

        // Mover la cámara directamente a la ubicación especificada con un nivel de zoom
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15f))



        /*map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(marker, 18f),
            4000, null
        )

        map.setOnMapClickListener { latLong ->
            map.clear()
            map.addMarker(MarkerOptions().
                position(latLong).title("Seleccionado")
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15f))

            val lat = latLong.latitude
            val lng = latLong.longitude

            Toast.makeText(this, "Lat: $lat, Lng: $lng", Toast.LENGTH_SHORT)
        }*/
    }
}