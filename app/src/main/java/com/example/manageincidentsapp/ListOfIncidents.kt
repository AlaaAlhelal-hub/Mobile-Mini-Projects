package com.example.manageincidentsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.manageincidentsapp.databinding.ActivityListOfIncidentsBinding
import com.example.manageincidentsapp.incident.listOfIncident.ListOfIncidentViewModel
import com.example.manageincidentsapp.incident.viewIncidentDetails.ViewIncidentViewModel

class ListOfIncidents : AppCompatActivity() {

    lateinit var listOfIncidentsViewModel: ListOfIncidentViewModel
    lateinit var incidentsViewModel: ViewIncidentViewModel

    lateinit var  navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityListOfIncidentsBinding = ActivityListOfIncidentsBinding.inflate(layoutInflater)

        /*
        incidentsViewModel = ViewModelProvider(this).get(ViewIncidentViewModel::class.java)

        listOfIncidentsViewModel = ViewModelProvider(this).get(ListOfIncidentViewModel::class.java)

        listOfIncidentsViewModel.navigateToIncidentDetails.observe(this, Observer { newIncident ->
            Log.i("observe value of incident","${newIncident.toString()}")
            incidentsViewModel.incident.value = newIncident
        })
*/


        drawerLayout = binding.drawerLayout

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.NavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)



        setContentView(binding.root)

    }



    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }


}