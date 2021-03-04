package com.space.astronaut.astronautinfo

import com.space.astronaut.di.ActivityScope
import com.space.astronaut.di.ViewModelModule
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelModule::class])
@ActivityScope
interface AstronautInfoComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AstronautInfoComponent
    }

    fun inject(astronautInfoActivity: AstronautInfoActivity)
}