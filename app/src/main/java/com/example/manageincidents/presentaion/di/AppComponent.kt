package com.example.manageincidents.presentaion.di

/*
import android.content.Context
import android.os.UserManager
import com.example.manageincidents.data.utils.ApiErrorResponse
import com.example.manageincidents.presentaion.di.modules.*
import com.example.manageincidents.presentaion.utils.LocaleUtil
import com.example.manageincidents.presentaion.utils.PreferenceUtil
import com.example.manageincidents.presentaion.utils.Session
import com.squareup.moshi.Moshi
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import dagger.hilt.DefineComponent
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        AppModule::class,
        ActivityBuilder::class,
        NetworkModule::class,
        CoroutinesModule::class,
        NetworkModule::class,
        RetrofitServiceModule::class,
        ViewModelModule::class
    ]
)

interface AppComponent {

    @DefineComponent.Builder
    interface Builder {
        fun application(@BindsInstance application: IncidentApp): Builder
        fun build(): AppComponent
    }

    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun context(): Context

    fun moshi(): Moshi

    fun preferenceUtil(): PreferenceUtil

    fun localeUtil(): LocaleUtil

    fun session(): Session

    fun retrofit(): Retrofit

    fun responseBodyConverter(): Converter<ResponseBody, ApiErrorResponse>

    //fun incidentsComponent(): IncidentsComponent.Factory

    //fun loginComponent(): LoginComponent.Factory

    fun userManager(): UserManager
}

*/