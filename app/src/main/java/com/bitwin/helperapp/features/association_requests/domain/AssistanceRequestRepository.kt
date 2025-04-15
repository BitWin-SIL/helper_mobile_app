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
                requesterId = "requester1",
                respondentId = "user1@example.com",
                requestType = "TYPE_A",
                status = AssistanceRequestStatus.Pending.name,
                message = "Request 1",
                createdAt = Date(),
                acceptedAt = null,
                completedAt = null
            ),
            AssistanceRequest(
                id = "2",
                requesterId = "requester2",
                respondentId = "accepted@example.com",
                requestType = "TYPE_B",
                status = AssistanceRequestStatus.Accepted.name,
                message = "Request 2",
                createdAt = Date(System.currentTimeMillis() - 86400000),
                acceptedAt = Date(System.currentTimeMillis() - 86000000),
                completedAt = null
            ),
            AssistanceRequest(
                id = "3",
                requesterId = "requester3",
                respondentId = "rejected@example.com",
                requestType = "TYPE_C",
                status = AssistanceRequestStatus.Rejected.name,
                message = "Request 3",
                createdAt = Date(System.currentTimeMillis() - 172800000),
                acceptedAt = null,
                completedAt = null
            )
        )
        emit(Resource.Success(sampleRequests))

        // Real API call commented out for testing
        /*
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
            requesterId = "requester_new",
            respondentId = email,
            requestType = "TYPE_A",
            status = AssistanceRequestStatus.Pending.name,
            message = "New request",
            createdAt = Date(),
            acceptedAt = null,
            completedAt = null
        )

        emit(Resource.Success(sampleRequest))

        // Real API call commented out for testing
        /*
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
