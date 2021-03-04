package com.space.astronaut.di

import android.content.Context
import com.space.astronaut.astronautinfo.AstronautInfoComponent
import com.space.astronaut.astronautlist.AstronautListComponent
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
    ViewModelFactoryModule::class, NetworkModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
    fun astronautListComponent(): AstronautListComponent.Factory
    fun astronautInfoComponent(): AstronautInfoComponent.Factory


}