package com.bitwin.helperapp.features.association_requests.logic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.association_requests.domain.AssistanceRequestRepository
import com.bitwin.helperapp.features.association_requests.ui.AssistanceRequest as UiAssistanceRequest
import com.bitwin.helperapp.features.association_requests.ui.AssistanceRequestStatus
import com.bitwin.helperapp.features.association_requests.data.AssistanceRequest as DataAssistanceRequest
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
    val requests: List<UiAssistanceRequest> = emptyList(),
    val sentRequestSuccess: Boolean = false
)

@HiltViewModel
class AssistanceRequestsViewModel @Inject constructor(
    private val repository: AssistanceRequestRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AssistanceRequestsUiState())
    val uiState: StateFlow<AssistanceRequestsUiState> = _uiState.asStateFlow()
    
    private val _requests = MutableStateFlow<List<UiAssistanceRequest>>(emptyList())
    val requests: StateFlow<List<UiAssistanceRequest>> = _requests

    init {
        fetchAssistanceRequests()
    }

    // Extension function to convert from data to UI model
    private fun DataAssistanceRequest.toUiModel(): UiAssistanceRequest {
        return UiAssistanceRequest(
            id = this.id,
            email = this.requesterId,
            date = this.createdAt,
            status = when(this.status.lowercase()) {
                "accepted" -> AssistanceRequestStatus.Accepted
                "rejected" -> AssistanceRequestStatus.Rejected
                else -> AssistanceRequestStatus.Pending
            }
        )
    }

    fun fetchAssistanceRequests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            Log.d("AssistanceRequestsVM", "Fetching assistance requests...")
            
            repository.getAssistanceRequests().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        val dataList = result.data ?: emptyList()
                        Log.d("AssistanceRequestsVM", "Received ${dataList.size} requests from repository: $dataList")
                        
                        if (dataList.isEmpty()) {
                            Log.d("AssistanceRequestsVM", "No requests received from repository")
                        }
                        
                        val uiModels = dataList.map { 
                            try {
                                val model = it.toUiModel()
                                Log.d("AssistanceRequestsVM", "Mapped request: $model")
                                model
                            } catch (e: Exception) {
                                Log.e("AssistanceRequestsVM", "Error mapping request: ${e.message}", e)
                                null
                            }
                        }.filterNotNull()
                        
                        Log.d("AssistanceRequestsVM", "Final UI models: $uiModels")
                        
                        _uiState.update { it.copy(
                            isLoading = false,
                            requests = uiModels,
                            error = null
                        ) }
                        _requests.value = uiModels
                    }
                    is Resource.Error -> {
                        Log.e("AssistanceRequestsVM", "Error fetching requests: ${result.message}")
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = result.message
                        ) }
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
