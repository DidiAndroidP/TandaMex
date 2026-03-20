package com.didiermendoza.tandamex.src.features.Home.domain.usecases

import com.didiermendoza.tandamex.src.features.Home.domain.repositories.HomeRepository
import javax.inject.Inject

class CheckWalletExistsUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(userId: Int): Boolean {
        return repository.checkWalletExists(userId)
    }
}