package com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.didiermendoza.tandamex.src.features.Profile.domain.usecases.GetMyProfileUseCase
import com.didiermendoza.tandamex.src.features.Tanda.domain.usecases.*

class TandaViewModelFactory(
    private val getTandaDetailUseCase: GetTandaDetailUseCase,
    private val joinTandaUseCase: JoinTandaUseCase,
    private val createTandaUseCase: CreateTandaUseCase,
    private val getTandaMembersUseCase: GetTandaMembersUseCase,
    private val leaveTandaUseCase: LeaveTandaUseCase,
    private val startTandaUseCase: StartTandaUseCase,
    private val finishTandaUseCase: FinishTandaUseCase,
    private val deleteTandaUseCase: DeleteTandaUseCase,
    private val generateScheduleUseCase: GenerateScheduleUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TandaViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                TandaViewModel(
                    getTandaDetailUseCase,
                    joinTandaUseCase,
                    getTandaMembersUseCase,
                    leaveTandaUseCase,
                    startTandaUseCase,
                    finishTandaUseCase,
                    deleteTandaUseCase,
                    generateScheduleUseCase,
                    getMyProfileUseCase
                ) as T
            }
            modelClass.isAssignableFrom(CreateTandaViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                CreateTandaViewModel(createTandaUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}