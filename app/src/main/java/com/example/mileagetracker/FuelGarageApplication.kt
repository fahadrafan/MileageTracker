package com.example.mileagetracker

import android.app.Application
import com.example.mileagetracker.di.AppContainer

class FuelGarageApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}