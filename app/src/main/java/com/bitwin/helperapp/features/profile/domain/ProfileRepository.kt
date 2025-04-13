package com.bitwin.helperapp.features.profile.domain

import com.bitwin.helperapp.core.api.HelperApi
import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.profile.data.UserInfoRequest
import com.bitwin.helperapp.features.profile.data.UserInfoResponse
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val helperApi: HelperApi
) {
    suspend fun getUserInfo(userId: String): Resource<UserInfoResponse> {
        return try {
            // FOR DEMO ONLY: Replace with actual API call when backend is ready
            delay(1000)
            Resource.Success(
                UserInfoResponse(
                    id = userId.toInt(),
                    firstName = "Mohamed",
                    lastName = "Racim",
                    email = "racim@example.com"
                )
            )
            
            // Uncomment when backend is ready:
            // val request = UserInfoRequest(userId)
            // val response = helperApi.getUserInfo(request)
            // Resource.Success(response)
        } catch (e: HttpException) {
            Resource.Error("Une erreur s'est produite: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Impossible d'atteindre le serveur. Vérifiez votre connexion Internet.")
        } catch (e: Exception) {
            Resource.Error("Une erreur s'est produite: ${e.message}")
        }
    }
}
