package com.example.manageincidents

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.manageincidents.databinding.ActivityListOfIncidentsBinding
import com.example.manageincidents.presentaion.app.verifyOTP.OtpVerificationActivity
import com.example.manageincidents.presentaion.app.viewListOfIncident.ListOfIncidentViewModel
import com.example.manageincidents.presentaion.app.viewIncidentDetails.ViewIncidentViewModel
import com.example.manageincidents.presentaion.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListOfIncidents : AppCompatActivity() {


    companion object {
        fun startListOfIncidentsActivity(context: Context) {
            val intent = Intent(context, ListOfIncidents::class.java)
            context.startActivity(intent)
        }
        fun newIntent(context: Context): Intent {
            return Intent(context, ListOfIncidents::class.java)
        }

        fun startListOfIncidentsClearStack(context: Context) {
            val intent = Intent(context, ListOfIncidents::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    lateinit var  navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityListOfIncidentsBinding = ActivityListOfIncidentsBinding.inflate(layoutInflater)

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