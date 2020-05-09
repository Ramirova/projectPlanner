package com.example.projectplanner.di

import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(gridPresenter : GridPresenter)
}