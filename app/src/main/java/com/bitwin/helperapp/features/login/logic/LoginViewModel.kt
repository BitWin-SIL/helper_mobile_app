package com.bitwin.helperapp.features.login.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitwin.helperapp.core.session.UserSessionManager
import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.login.data.LoginRequest
import com.bitwin.helperapp.features.login.domain.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userSessionManager: UserSessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun loginUser(request: LoginRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            when (val result = loginRepository.login(request)) {
                is Resource.Success -> {
                    result.data?.id?.let { userSessionManager.saveUserSession(it.toString()) }
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
            }
        }
    }
}