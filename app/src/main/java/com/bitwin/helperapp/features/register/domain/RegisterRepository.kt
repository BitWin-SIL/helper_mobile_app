package com.bitwin.helperapp.features.register.domain

import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.register.data.RegisterApi
import com.bitwin.helperapp.features.register.data.RegisterRequest
import com.bitwin.helperapp.features.register.data.RegisterResponse
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import retrofit2.HttpException
import java.io.IOException

@ActivityScoped
class RegisterRepository @Inject constructor(
    private val registerApi: RegisterApi
) {
    suspend fun register(request: RegisterRequest): Resource<RegisterResponse> {
        return try {
            val response = registerApi.register(request)
            Resource.Success(response)
        } catch (e: HttpException) {
            Resource.Error("Une erreur inconnue s'est produite : ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Impossible d'atteindre le serveur. Vérifiez votre connexion Internet.")
        }
    }
}
