package com.didiermendoza.tandamex.src.features.Tanda.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.didiermendoza.tandamex.src.features.Tanda.domain.repositories.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _showReviewDialog = MutableStateFlow(false)
    val showReviewDialog = _showReviewDialog.asStateFlow()

    fun onTandaFinished(isCreator: Boolean) {
        if (!isCreator) {
            _showReviewDialog.value = true
        }
    }

    fun submitReview(tandaId: Int, rating: Int, comment: String) {
        viewModelScope.launch {
            reviewRepository.createReview(tandaId, rating, comment)
            _showReviewDialog.value = false
        }
    }

    fun dismissDialog() {
        _showReviewDialog.value = false
    }
}