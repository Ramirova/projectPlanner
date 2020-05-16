package com.example.projectplanner

import android.app.Application
import com.example.projectplanner.di.DaggerApplicationComponent

class ProjectPlannerApplication : Application() {

    val appComponent = DaggerApplicationComponent.create()!!

    override fun onCreate() {
        super.onCreate()
        APPLICATION = this
    }

    companion object {
        lateinit var APPLICATION: Application
    }
}