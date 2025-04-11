package com.bitwin.helperapp.features.login.domain

import com.bitwin.helperapp.core.api.HelperApi
import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.login.data.LoginRequest
import com.bitwin.helperapp.features.login.data.LoginResponse
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.delay
import javax.inject.Inject
import retrofit2.HttpException
import java.io.IOException

@ActivityScoped
class LoginRepository @Inject constructor(
    private val helperApi: HelperApi
) {
    // suspend fun login(request: LoginRequest): Resource<LoginResponse> {
    //     return try {
    //         val response = helperApi.login(request)
    //         Resource.Success(response)
    //     } catch (e: HttpException) {
    //         Resource.Error("Une erreur s'est produite: ${e.message()}")
    //     } catch (e: IOException) {
    //         Resource.Error("Impossible d'atteindre le serveur. Vérifiez votre connexion Internet.")
    //     }
    // }
    suspend fun login(request: LoginRequest): Resource<LoginResponse> {

        delay(1000)
        return Resource.Success(LoginResponse(1, "fake_token"))
    }
}
