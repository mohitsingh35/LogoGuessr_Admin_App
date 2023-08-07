package com.ncs.logoguessradmin.di

import com.ncs.logoguessradmin.repository.RealtimeDBRepository
import com.ncs.logoguessradmin.repository.RealtimeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesRealtimeRepository(
        repo:RealtimeDBRepository
    ):RealtimeRepository
}