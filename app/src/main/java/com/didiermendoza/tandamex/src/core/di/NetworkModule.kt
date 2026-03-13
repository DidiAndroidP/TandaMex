package com.didiermendoza.tandamex.src.core.di

import com.didiermendoza.tandamex.BuildConfig
import com.didiermendoza.tandamex.src.core.storage.AuthInterceptor
import com.didiermendoza.tandamex.src.features.Home.data.datasource.remote.api.HomeApiService
import com.didiermendoza.tandamex.src.features.Login.data.datasource.remote.api.LoginApiService
import com.didiermendoza.tandamex.src.features.Profile.data.datasource.remote.api.ProfileApiService
import com.didiermendoza.tandamex.src.features.Register.data.datasource.remote.api.RegisterApiService
import com.didiermendoza.tandamex.src.features.Tanda.data.datasource.remote.api.TandaApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginApiService {
        return retrofit.create(LoginApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRegisterApi(retrofit: Retrofit): RegisterApiService {
        return retrofit.create(RegisterApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTandaApi(retrofit: Retrofit): TandaApiService {
        return retrofit.create(TandaApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApiService {
        return retrofit.create(HomeApiService::class.java)
    }
}