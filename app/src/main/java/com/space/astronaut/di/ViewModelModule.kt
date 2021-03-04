package com.space.astronaut.di

import androidx.lifecycle.ViewModel
import com.space.astronaut.astronautinfo.AstronautInfoViewModel
import com.space.astronaut.astronautlist.AstronautListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AstronautListViewModel::class)
    internal abstract fun bindUserInfoListViewModel(viewModel: AstronautListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AstronautInfoViewModel::class)
    internal abstract fun bindAstronautInfoViewModel(astronautInfoViewModel: AstronautInfoViewModel): ViewModel
}