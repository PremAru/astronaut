package com.space.astronaut.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}