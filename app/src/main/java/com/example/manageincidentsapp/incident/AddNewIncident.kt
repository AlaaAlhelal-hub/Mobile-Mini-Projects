package com.example.manageincidentsapp.incident

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.manageincidentsapp.ListOfIncidents
import com.example.manageincidentsapp.R
import com.example.manageincidentsapp.databinding.ActivityAddNewIncidentBinding
import com.example.manageincidentsapp.user.IncidentApiStatus
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class AddNewIncident : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var mapMarker: Marker
    private lateinit var incidentViewModel: AddNewIncidentViewModel
    private lateinit var binding: ActivityAddNewIncidentBinding
    private lateinit var typeSelected: String
    private var typeId: Int = 1
    private lateinit var subtypeSelected: String
    private var def_lat = 24.713968352762397
    private var def_lang = 46.6723135989098
    private var ZOOM_LEVEL = 15f
    private var ERROR_MESSAGE = "This field is required"
    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var attachmentFragment: AttachmentFragment
    private var imageUriUploaded: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNewIncidentBinding.inflate(layoutInflater)

        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.Map, mapFragment)
            .commit()
        mapFragment.getMapAsync(this)



        incidentViewModel = ViewModelProvider(this).get(AddNewIncidentViewModel::class.java)

        binding.lifecycleOwner = this

        binding.incidentViewModel = incidentViewModel


        incidentViewModel.getTypeStatus.observe(this, androidx.lifecycle.Observer { newStatus ->
            if (newStatus == IncidentApiStatus.Done) {
                val typeSpinner: Spinner = binding.type
                val typeList = ArrayList<String>()

                for (i in 0 until incidentViewModel.incidentType.value!!.size){
                    typeList.add(incidentViewModel.incidentType.value!![i].englishName)
                }
                val arrayAdapter  = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    typeList
                )

                typeSpinner.adapter = arrayAdapter
                setSubtypeSpinner(0)


                binding.subType.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, p3: Long) {
                        subtypeSelected = parent?.getItemAtPosition(pos).toString()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        TODO("Not yet implemented")
                    }

                }
                typeSpinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
                    override fun onItemSelected(parent: AdapterView<*>,
                                                view: View, position: Int, id: Long) {
                        setSubtypeSpinner(position)
                        typeId = incidentViewModel.incidentType.value!![position].id
                        typeSelected = parent.getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        TODO("Not yet implemented")                    }

                    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        TODO("Not yet implemented")
                    }
                }
            }

        })

        binding.addNewIncidentBtn.setOnClickListener {

            if (InputValidation()) {
                val description = binding.indidentDescription.text.toString()
                val latitude = mapMarker.position.latitude
                val longitude = mapMarker.position.longitude
                val type_Id = typeId

               incidentViewModel.postNewIncident(description,type_Id,latitude,longitude)
            }
        }

        binding.uploadMediaBtn.setOnClickListener {
            openGalleryForImage()
        }

        incidentViewModel.status.observe(this, androidx.lifecycle.Observer {


            if (it == IncidentApiStatus.Done) {
                Toast.makeText(this,"added ",Toast.LENGTH_LONG).show()
                val attachmentFragment =  supportFragmentManager.findFragmentById(R.id.row_attachment_fragment) as AttachmentFragment
                val imagePicked = attachmentFragment.view?.findViewById<ImageView>(R.id.attachment_imageView)

                if (imageUriUploaded != null && imagePicked!!.isVisible){

                    incidentViewModel.incidentId.value?.let { it1 ->
                        incidentViewModel.uploadIncidentPhotos(
                            it1, imageUriUploaded!!
                        )
                    }
                }
            }
        })


        incidentViewModel.uploadStatus.observe(this, androidx.lifecycle.Observer { newStatus ->

            if (newStatus == IncidentApiStatus.Done) {

                val listofIncident = Intent(this, ListOfIncidents::class.java)
                startActivity(listofIncident)

            }

            })

            setContentView(binding.root)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableMyLocation()
        Log.i("MyLocationEnabled = ","${map.isMyLocationEnabled}")
        var location = LatLng(def_lat, def_lang)
        if(map.isMyLocationEnabled) {
            map.setOnMyLocationChangeListener(OnMyLocationChangeListener { arg0 ->
                location = LatLng(arg0.latitude, arg0.longitude)
            })
        }

        mapMarker = map.addMarker(MarkerOptions()
            .position(location)
            .title("your location")
            .draggable(true))!!
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM_LEVEL))
        map.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL), 2000, null)

        setMapLongClick(map)

    }

    private fun setMapLongClick(map: GoogleMap) {

        map.setOnMapLongClickListener { latLng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )

            mapMarker.remove()
            mapMarker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .draggable(true)
                    .title("your location")
                    .snippet(snippet)
            )!!

            map.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        }
    }


    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            map.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.size > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }




    fun InputValidation(): Boolean {
        val incidentDesc = binding.indidentDescription

        if(incidentDesc.text.isEmpty()) {
            incidentDesc.error = ERROR_MESSAGE
            return false
        }
        return true
    }

    fun setSubtypeSpinner(Id: Int){

        val subTypeSpinner: Spinner = binding.subType
        val subtypeList = ArrayList<String>()

        val superType = incidentViewModel.incidentType.value!![Id].subTypes
        for (i in 0 until superType!!.size){
            subtypeList.add(superType[i].englishName)
        }
        val arrayAdapter  = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            subtypeList
        )

        subTypeSpinner.adapter = arrayAdapter
        subtypeSelected = subtypeList[0]

    }


    val REQUEST_CODE = 100

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            attachmentFragment = supportFragmentManager.findFragmentById(R.id.row_attachment_fragment) as AttachmentFragment
            val imagePicked = attachmentFragment.view?.findViewById<ImageView>(R.id.attachment_imageView)
            imagePicked?.visibility= View.VISIBLE
            imagePicked?.setImageURI(data?.data)
            imageUriUploaded = data?.data


            val imageDeleteIcon = attachmentFragment.view?.findViewById<ImageView>(R.id.deleteAttachment)
            imageDeleteIcon?.visibility= View.VISIBLE
            Log.i("image Delete Icon visibility" , "${imageDeleteIcon?.isVisible}")
        }
    }

}
