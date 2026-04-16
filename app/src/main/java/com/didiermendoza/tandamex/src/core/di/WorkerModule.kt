package com.didiermendoza.tandamex.src.core.di

import com.didiermendoza.tandamex.src.core.workers.ProfileSyncManager
import com.didiermendoza.tandamex.src.core.workers.WorkManagerProfileSyncImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerModule {

    @Binds
    @Singleton
    abstract fun bindPhotoUploadScheduler(
        impl: WorkManagerProfileSyncImpl
    ): ProfileSyncManager
}