package com.example.manageincidents.presentaion.di



import androidx.multidex.MultiDexApplication
import com.example.manageincidents.presentaion.utils.LocaleUtil
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class IncidentApp : MultiDexApplication() {

    @Inject
    lateinit var localeUtil: LocaleUtil


    override fun onCreate() {
        super.onCreate()
        localeUtil.initAppLocale()
    }


    companion object {
        private var instance: IncidentApp? = null
        fun getInstance(): IncidentApp? {
            if (instance == null) {
                synchronized(IncidentApp::class.java) {
                    if (instance == null) instance = IncidentApp()
                }
            }
            // Return the instance
            return instance
        }
    }



}
