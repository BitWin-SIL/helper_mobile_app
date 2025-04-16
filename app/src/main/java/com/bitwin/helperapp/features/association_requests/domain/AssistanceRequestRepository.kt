package com.bitwin.helperapp.features.association_requests.domain

import android.util.Log
import com.bitwin.helperapp.core.api.HelperApi
import com.bitwin.helperapp.core.session.UserSessionManager
import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.association_requests.data.AssistanceRequest
import com.bitwin.helperapp.features.association_requests.data.CreateAssistanceRequestBody
import com.bitwin.helperapp.features.association_requests.data.CreateAssistanceRequestResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AssistanceRequestRepository @Inject constructor(
    private val api: HelperApi
) {
    fun getAssistanceRequests(): Flow<Resource<List<AssistanceRequest>>> = flow {
        emit(Resource.Loading())
        try {
            val userId = UserSessionManager.getUserId()
            Log.d("AssistanceRepo", "Getting requests for user: $userId")
            
            // Make sure we're filtering correctly - try without the "eq." prefix if necessary
            val filteredUserId = try {
                // Try to convert to number to match API expectation
                "eq.${userId.toInt()}"
            } catch (e: Exception) {
                "eq.$userId"
            }
            
            Log.d("AssistanceRepo", "Using filter: $filteredUserId")
            
            // Direct call to get array of request DTOs
            val responseData = api.getAssistanceRequests(
                select = "id,requester_id,respondent_id,request_type,status,message,location_id,created_at,accepted_at,completed_at",
                requesterId = filteredUserId
            )
            
            Log.d("AssistanceRepo", "API Response: received ${responseData.size} items")
            
            val domainRequests = responseData.map { 
                try {
                    val domain = it.toDomainModel()
                    Log.d("AssistanceRepo", "Mapped domain model: $domain")
                    domain
                } catch (e: Exception) {
                    Log.e("AssistanceRepo", "Error mapping DTO to domain: ${e.message}", e)
                    null
                }
            }.filterNotNull()
            
            Log.d("AssistanceRepo", "Final domain models: ${domainRequests.size}")
            emit(Resource.Success(domainRequests))
            
        } catch (e: HttpException) {
            Log.e("AssistanceRepo", "HTTP Exception: ${e.message}", e)
            emit(Resource.Error("Erreur réseau: ${e.code()}: ${e.message() ?: "Une erreur est survenue"}"))
        } catch (e: IOException) {
            Log.e("AssistanceRepo", "IO Exception: ${e.message}", e)
            emit(Resource.Error("Vérifiez votre connexion internet"))
        } catch (e: Exception) {
            Log.e("AssistanceRepo", "General Exception: ${e.message}", e)
            // Improve error messaging for JSON parsing errors
            val errorMessage = when {
                e.message?.contains("Expected BEGIN_OBJECT but was BEGIN_ARRAY") == true -> 
                    "Erreur de format dans la réponse de l'API"
                else -> "Une erreur est survenue: ${e.message}"
            }
            emit(Resource.Error(errorMessage))
        }
    }

    fun createAssistanceRequest(email: String): Flow<Resource<CreateAssistanceRequestResponse>> = flow {
        emit(Resource.Loading())
        try {
            val requestBody = CreateAssistanceRequestBody(
                visuallyImpairedEmail = email,
                visuallyImpairedId = "2",
                visuallyImpairedName = "John Doe",
                message = "I would like to be your helper."
            )
            val response = api.createAssistanceRequest(requestBody)
            if (response.success) {
                emit(Resource.Success(response))
            } else {
                emit(Resource.Error("Request failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}
