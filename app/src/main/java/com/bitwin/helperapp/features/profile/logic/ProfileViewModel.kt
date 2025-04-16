package com.bitwin.helperapp.features.profile.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitwin.helperapp.core.session.UserSessionManager
import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.profile.data.UserInfoResponse
import com.bitwin.helperapp.features.profile.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userInfo: UserInfoResponse? = null,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadUserInfo()
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val userId = UserSessionManager.getUserId()

            when (val result = profileRepository.getUserInfo(userId)) {
                is Resource.Success -> {
                    _uiState.update { it.copy(isLoading = false, userInfo = result.data) }
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
    
    fun logout() {
        UserSessionManager.clearSession()
    }
}
