package com.mik.recyloc


import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_addlocdialog.view.*
import kotlinx.android.synthetic.main.fragment_maps.*


class MapsFragment : Fragment(), OnMapReadyCallback,LocationListener {

    private var lat : Double = 0.0
    private var lng : Double = 0.0
    private var isLocationPicked = false
    private var isCurrentLocationPicked = false
    private var permissionControl = 0

    private val locationProvider = "gps"

    private lateinit var locationManager : LocationManager
    private lateinit var mMap: GoogleMap
    private lateinit var currentLocationMarker : Marker
    private lateinit var clickedLocationMarker : Marker


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        checkPermission()
        readDataFromFirestore()
        onClickFindLocationButton()
        onClickAddLocationButton()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val center = LatLng(0.0, 0.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(center))

        findCurrentLocation()

        googleMap.setOnMapClickListener {
            if (isLocationPicked) {
                clickedLocationMarker.remove()
            }
            clickedLocationMarker = googleMap.addMarker(MarkerOptions().position(it).title("Clicked location."))!!
            clickedLocationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            isLocationPicked = true
            lat = it.latitude
            lng = it.longitude
        }

        googleMap.setInfoWindowAdapter(object : InfoWindowAdapter {
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                val info = LinearLayout(requireContext())
                info.orientation = LinearLayout.VERTICAL
                val title = TextView(requireContext())
                title.setTextColor(Color.BLACK)
                title.gravity = Gravity.CENTER
                title.setTypeface(null, Typeface.BOLD)
                title.text = marker.title
                val snippet = TextView(requireContext())
                snippet.setTextColor(Color.GRAY)
                snippet.text = marker.snippet
                info.addView(title)
                info.addView(snippet)
                return info
            }
        })

    }

    override fun onLocationChanged(p0: Location) {

        if (isCurrentLocationPicked) {
            currentLocationMarker.remove()
        }

        val latitude = p0.latitude
        val longitude = p0.longitude

        val currentLocation = LatLng(latitude,longitude)
        val zoomLevel = 15f

        currentLocationMarker = mMap.addMarker(
            MarkerOptions()
                .position(currentLocation)
                .title("Current location"))!!
        isCurrentLocationPicked = true
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,zoomLevel))
    }

    private fun onClickFindLocationButton() {
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        find_location_button.setOnClickListener {
            findCurrentLocation()
        }

    }

    private fun checkPermission() {
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        permissionControl = ContextCompat
            .checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            )
        if (permissionControl != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100
            )

        }
    }

    private fun findCurrentLocation() {
        permissionControl = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionControl != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }else {
            val location = locationManager.getLastKnownLocation(locationProvider)
            if (location != null) {
                onLocationChanged(location)
            }else {
                Toast.makeText(requireContext(),"Error with finding location.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onClickAddLocationButton() {
        add_location_button.setOnClickListener {
            val design = layoutInflater.inflate(R.layout.fragment_addlocdialog,null)
            val newAlert = AlertDialog.Builder(requireContext())
            newAlert.setView(design)
            newAlert.setPositiveButton("Add") {_,_ ->
                var plastic = "false"
                var glass = "false"
                var paper = "false"
                var composite = "false"
                var battery = "false"
                var general = "false"

                if (design.radioButtonGeneral.isChecked) {
                    general = "true"
                }
                if(design.radioButtonPlastic.isChecked) {
                    plastic = "true"
                }
                if (design.radioButtonGlass.isChecked) {
                    glass = "true"
                }
                if (design.radioButtonPaper.isChecked) {
                    paper = "true"
                }

                if (design.radioButtonComposite.isChecked) {
                    composite = "true"
                }

                if (design.radioButtonBattery.isChecked) {
                    battery = "true"
                }

                val location = hashMapOf<String, Any>(
                    "latitude" to lat,
                    "longitude" to lng,
                    "general" to general,
                    "plastic" to plastic,
                    "glass" to glass ,
                    "paper" to paper ,
                    "composite" to composite,
                    "battery" to battery,
                    "approved" to "false"
                )

                FirebaseUtils().fireStoreDatabase.collection("locations")
                    .add(location)
                    .addOnSuccessListener {
                        Log.d(TAG, "Added document with ID ${it.id}")
                        Toast.makeText(requireContext(),"Succesful! Waiting for approval!",Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error adding document $exception")
                    }
            }

            newAlert.setNegativeButton("Cancel") {_,_ ->
                Toast.makeText(requireContext(),"Cancel",Toast.LENGTH_SHORT).show()
            }
            newAlert.create().show()
        }
    }

    private fun readDataFromFirestore() {
        FirebaseUtils().fireStoreDatabase.collection("locations")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val latitude = document.get("latitude")
                    val longitude = document.get("longitude")
                    val general = document.get("general")
                    val plastic = document.get("plastic")
                    val glass = document.get("glass")
                    val paper = document.get("paper")
                    val composite = document.get("composite")
                    val battery = document.get("battery")
                    val approved = document.get("approved")

                    if(approved == "true") {
                        mMap.addMarker(MarkerOptions()
                            .position(LatLng(latitude.toString().toDouble(),longitude.toString().toDouble()))
                            .title("Recycle Bin(s)")
                            .snippet("General : $general" +
                                    "\n"+
                                    "Plastic : $plastic " +
                                    "\n"+
                                    "Glass : $glass " +
                                    "\n"+
                                    "Paper : $paper " +
                                    "\n"+
                                    "Composite : $composite " +
                                    "\n"+
                                    "Battery : $battery ")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                    }

                }
            }
    }
}

