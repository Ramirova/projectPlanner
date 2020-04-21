package com.example.projectplanner

import com.activeandroid.app.Application
import com.example.projectplanner.di.AppComponent

class ProjectPlannerApplication : Application() {
    companion object {
        lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

//        graph = DaggerAppComponent.builder().noteDaoModule(DaoModule()).build()
        // Любая ошибка компиляции не дает даггеру создасться https://ru.stackoverflow.com/questions/660900/error-cannot-find-symbol-class-daggerappcomponent
    }
}