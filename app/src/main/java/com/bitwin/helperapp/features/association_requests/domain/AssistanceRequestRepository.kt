package com.bitwin.helperapp.features.association_requests.domain

import com.bitwin.helperapp.core.api.HelperApi
import com.bitwin.helperapp.core.utilities.Resource
import com.bitwin.helperapp.features.association_requests.data.AssistanceRequest
import com.bitwin.helperapp.features.association_requests.data.CreateAssistanceRequestBody
import com.bitwin.helperapp.features.association_requests.ui.AssistanceRequestStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.Date
import javax.inject.Inject

class AssistanceRequestRepository @Inject constructor(
    private val api: HelperApi
) {
    fun getAssistanceRequests(): Flow<Resource<List<AssistanceRequest>>> = flow {
        emit(Resource.Loading())
        delay(1000)
        
        val sampleRequests = listOf(
            AssistanceRequest(
                id = "1",
                email = "user1@example.com",
                date = Date(),
                status = AssistanceRequestStatus.Pending.name
            ),
            AssistanceRequest(
                id = "2",
                email = "accepted@example.com",
                date = Date(System.currentTimeMillis() - 86400000),
                status = AssistanceRequestStatus.Accepted.name
            ),
            AssistanceRequest(
                id = "3",
                email = "rejected@example.com",
                date = Date(System.currentTimeMillis() - 172800000),
                status = AssistanceRequestStatus.Rejected.name
            )
        )
        emit(Resource.Success(sampleRequests))
        
        /* Real API call commented out for testing
        try {
            emit(Resource.Loading())
            val response = api.getAssistanceRequests()
            
            if (response.status == "success") {
                val domainRequests = response.data.map { it.toDomainModel() }
                emit(Resource.Success(domainRequests))
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Erreur réseau: ${e.message ?: "Une erreur est survenue"}"))
        } catch (e: IOException) {
            emit(Resource.Error("Vérifiez votre connexion internet"))
        } catch (e: Exception) {
            emit(Resource.Error("Une erreur est survenue: ${e.message}"))
        }
        */
    }

    fun createAssistanceRequest(email: String): Flow<Resource<AssistanceRequest?>> = flow {
        emit(Resource.Loading())
        
        delay(1500)
        
        val sampleRequest = AssistanceRequest(
            id = "new-${System.currentTimeMillis()}",
            email = email,
            date = Date(),
            status = AssistanceRequestStatus.Pending.name
        )
        
        emit(Resource.Success(sampleRequest))
        
        /* Real API call commented out for testing
        try {
            emit(Resource.Loading())
            val body = CreateAssistanceRequestBody(respondentEmail = email)
            val response = api.createAssistanceRequest(body)
            
            if (response.status == "success") {
                val domainRequest = response.data?.toDomainModel()
                emit(Resource.Success(domainRequest))
            } else {
                emit(Resource.Error(response.message))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Erreur réseau: ${e.message ?: "Une erreur est survenue"}"))
        } catch (e: IOException) {
            emit(Resource.Error("Vérifiez votre connexion internet"))
        } catch (e: Exception) {
            emit(Resource.Error("Une erreur est survenue: ${e.message}"))
        }
        */
    }
}
