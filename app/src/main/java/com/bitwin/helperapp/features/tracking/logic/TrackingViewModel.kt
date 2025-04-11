package com.bitwin.helperapp.features.tracking.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.tracking.data.TrackingRequest
import com.bitwin.helperapp.features.tracking.data.TrackingResponse
import com.bitwin.helperapp.features.tracking.domain.TrackingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TrackingUiState(
    val isLoading: Boolean = false,
    val response: TrackingResponse? = null,
    val error: String? = null
)

@HiltViewModel
class TrackingViewModel @Inject constructor(
    private val trackingRepository: TrackingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackingUiState())
    val uiState: StateFlow<TrackingUiState> = _uiState

    fun trackPosition(request: TrackingRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when(val result = trackingRepository.track(request)) {
                is Resource.Success -> _uiState.update { it.copy(isLoading = false, response = result.data) }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
            }
        }
    }
}
