package com.didiermendoza.tandamex.src.core.di

import com.didiermendoza.tandamex.src.features.Home.data.repositories.HomeRepositoryImpl
import com.didiermendoza.tandamex.src.features.Home.domain.repositories.HomeRepository
import com.didiermendoza.tandamex.src.features.Login.data.repositories.LoginRepositoryImpl
import com.didiermendoza.tandamex.src.features.Login.domain.repositories.LoginRepository
import com.didiermendoza.tandamex.src.features.Profile.data.repositories.ProfileRepositoryImpl
import com.didiermendoza.tandamex.src.features.Profile.domain.repositories.ProfileRepository
import com.didiermendoza.tandamex.src.features.Register.data.repositories.RegisterRepositoryImpl
import com.didiermendoza.tandamex.src.features.Register.domain.repositories.RegisterRepository
import com.didiermendoza.tandamex.src.features.Tanda.data.repositories.TandaRepositoryImpl
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.TandaRepository
import com.didiermendoza.tandamex.src.features.Tanda.data.repositories.ReviewRepositoryImpl
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.ReviewRepository
import com.didiermendoza.tandamex.src.features.wallet.data.repository.WalletRepositoryImpl
import com.didiermendoza.tandamex.src.features.wallet.domain.repository.WalletRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindRegisterRepository(
        registerRepositoryImpl: RegisterRepositoryImpl
    ): RegisterRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindTandaRepository(
        tandaRepositoryImpl: TandaRepositoryImpl
    ): TandaRepository

    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        walletRepositoryImpl: WalletRepositoryImpl
    ): WalletRepository

    @Binds
    @Singleton
    abstract fun bindReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository
}