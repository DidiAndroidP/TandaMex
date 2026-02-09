package com.didiermendoza.tandamex.src.features.Tanda.di

import com.didiermendoza.tandamex.src.core.di.AppContainer
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import com.didiermendoza.tandamex.src.features.Tanda.domain.usecases.*
import com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels.TandaViewModelFactory

class TandaModule(private val appContainer: AppContainer) {

    fun provideTandaViewModelFactory(): TandaViewModelFactory {
        val repository = appContainer.tandaRepository
        val profileRepository = appContainer.profileRepository

        return TandaViewModelFactory(
            getTandaDetailUseCase = GetTandaDetailUseCase(repository),
            joinTandaUseCase = JoinTandaUseCase(repository),
            createTandaUseCase = CreateTandaUseCase(repository),
            getTandaMembersUseCase = GetTandaMembersUseCase(repository),
            leaveTandaUseCase = LeaveTandaUseCase(repository),
            startTandaUseCase = StartTandaUseCase(repository),
            finishTandaUseCase = FinishTandaUseCase(repository),
            deleteTandaUseCase = DeleteTandaUseCase(repository),
            generateScheduleUseCase = GenerateScheduleUseCase(repository),
            getMyProfileUseCase = GetMyProfileUseCase(profileRepository)
        )
    }

    fun provideCreateTandaViewModelFactory(): TandaViewModelFactory {
        return provideTandaViewModelFactory()
    }
}