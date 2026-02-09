package com.didiermendoza.tandamex.src.core.di

import android.content.Context
import com.didiermendoza.tandamex.BuildConfig
import com.didiermendoza.tandamex.src.core.http.AuthInterceptor
import com.didiermendoza.tandamex.src.core.http.TandaMexApi
import com.didiermendoza.tandamex.src.core.storage.TokenManager
import com.didiermendoza.tandamex.src.features.Home.data.repositories.HomeRepositoryImpl
import com.didiermendoza.tandamex.src.features.Home.di.HomeModule
import com.didiermendoza.tandamex.src.features.Home.domain.repositories.HomeRepository
import com.didiermendoza.tandamex.src.features.Login.data.repositories.LoginRepositoryImpl
import com.didiermendoza.tandamex.src.features.Login.di.LoginModule
import com.didiermendoza.tandamex.src.features.Login.domain.repositories.LoginRepository
import com.didiermendoza.tandamex.src.features.Profile.data.repositories.ProfileRepositoryImpl
import com.didiermendoza.tandamex.src.features.Profile.di.ProfileModule
import com.didiermendoza.tandamex.src.features.Profile.domain.repositories.ProfileRepository
import com.didiermendoza.tandamex.src.features.Register.data.repositories.RegisterRepositoryImpl
import com.didiermendoza.tandamex.src.features.Register.di.RegisterModule
import com.didiermendoza.tandamex.src.features.Register.domain.repositories.RegisterRepository
import com.didiermendoza.tandamex.src.features.Tanda.data.repositories.TandaRepositoryImpl
import com.didiermendoza.tandamex.src.features.Tanda.di.TandaModule
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(private val context: Context) {

    val tokenManager: TokenManager by lazy {
        TokenManager(context)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val tandaMexApi: TandaMexApi by lazy {
        retrofit.create(TandaMexApi::class.java)
    }

    val registerRepository: RegisterRepository by lazy {
        RegisterRepositoryImpl(tandaMexApi)
    }

    val loginRepository: LoginRepository by lazy {
        LoginRepositoryImpl(tandaMexApi, tokenManager)
    }

    val homeRepository: HomeRepository by lazy {
        HomeRepositoryImpl(tandaMexApi)
    }

    val tandaRepository: TandaRepository by lazy {
        TandaRepositoryImpl(tandaMexApi)
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(tandaMexApi)
    }

    val registerModule: RegisterModule by lazy {
        RegisterModule(this)
    }

    val loginModule: LoginModule by lazy {
        LoginModule(this)
    }

    val homeModule: HomeModule by lazy {
        HomeModule(this)
    }

    val profileModule: ProfileModule by lazy {
        ProfileModule(this)
    }

    val tandaModule: TandaModule by lazy {
        TandaModule(this)
    }
}