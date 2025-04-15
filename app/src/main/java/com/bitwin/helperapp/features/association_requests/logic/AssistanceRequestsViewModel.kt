package com.bitwin.helperapp.features.association_requests.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.association_requests.domain.AssistanceRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AssistanceRequestsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val requests: List<AssistanceRequest> = emptyList(),
    val sentRequestSuccess: Boolean = false
)

@HiltViewModel
class AssistanceRequestsViewModel @Inject constructor(
    private val repository: AssistanceRequestRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AssistanceRequestsUiState())
    val uiState: StateFlow<AssistanceRequestsUiState> = _uiState.asStateFlow()
    
    private val _requests = MutableStateFlow<List<AssistanceRequest>>(emptyList())
    val requests: StateFlow<List<AssistanceRequest>> = _requests

    init {
        fetchAssistanceRequests()
    }

    fun fetchAssistanceRequests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.getAssistanceRequests().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val uiModels = result.data?.map { it.toUiModel() } ?: emptyList()
                        _uiState.update { it.copy(
                            isLoading = false,
                            requests = uiModels,
                            error = null
                        ) }
                        _requests.value = uiModels
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = result.message
                        ) }
                        Log.e("AssistanceRequestsViewModel", "Error: ${result.message}")
                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun addRequest(email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, sentRequestSuccess = false) }
            repository.createAssistanceRequest(email).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            sentRequestSuccess = true,
                            error = null
                        ) }
                        // Refresh the list after adding a request
                        fetchAssistanceRequests()
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = result.message,
                            sentRequestSuccess = false
                        ) }
                        Log.e("AssistanceRequestsViewModel", "Error: ${result.message}")
                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }
}
