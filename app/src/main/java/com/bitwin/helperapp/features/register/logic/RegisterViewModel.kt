package com.bitwin.helperapp.features.register.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.register.data.RegisterRequest
import com.bitwin.helperapp.features.register.domain.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            when (val result = registerRepository.register(request)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}