package com.didiermendoza.tandamex.src.core.di

import android.content.Context
import com.didiermendoza.tandamex.BuildConfig
import com.didiermendoza.tandamex.src.core.http.TandaMexApi
import com.didiermendoza.tandamex.src.features.Login.data.repositories.LoginRepositoryImpl
import com.didiermendoza.tandamex.src.features.Login.domain.repositories.LoginRepository
import com.didiermendoza.tandamex.src.features.Register.data.repositories.RegisterRepositoryImpl
import com.didiermendoza.tandamex.src.features.Register.domain.repositories.RegisterRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(private val context: Context) {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val tandaMexApi: TandaMexApi by lazy {
        retrofit.create(TandaMexApi::class.java)
    }

    val registerRepository: RegisterRepository by lazy {
        RegisterRepositoryImpl(tandaMexApi)
    }

    val loginRepository: LoginRepository by lazy {
        LoginRepositoryImpl(tandaMexApi)
    }
}