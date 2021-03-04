package com.space.astronaut.astronautlist

import com.space.astronaut.di.ActivityScope
import com.space.astronaut.di.ViewModelModule
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
@ActivityScope
interface AstronautListComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AstronautListComponent
    }

    fun inject(astronautListActivity: AstronautListActivity)
}