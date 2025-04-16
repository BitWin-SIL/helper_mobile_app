package com.bitwin.helperapp.core.di
import com.bitwin.helperapp.core.api.HelperApi
import com.bitwin.helperapp.core.utilities.Constant
import com.bitwin.helperapp.features.register.domain.RegisterRepository
import com.bitwin.helperapp.features.login.domain.LoginRepository
import com.bitwin.helperapp.features.profile.domain.ProfileRepository
import com.bitwin.helperapp.features.association_requests.domain.AssistanceRequestRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val headerInterceptor = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer ${Constant.SUPABASE_API_KEY}")
                .header("Content-Type", "application/json")
                .header("api-key", Constant.SUPABASE_API_KEY)
                .header("Prefer", "return=representation")
                .build()
            chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constant.SUPABASE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideHelperApi(retrofit: Retrofit): HelperApi {
        return retrofit.create(HelperApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRegisterRepository(helperApi: HelperApi): RegisterRepository {
        return RegisterRepository(helperApi)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(helperApi: HelperApi): LoginRepository {
        return LoginRepository(helperApi)
    }
    
    @Singleton
    @Provides
    fun provideTrackingRepository(): com.bitwin.helperapp.features.tracking.domain.TrackingRepository {
        return com.bitwin.helperapp.features.tracking.domain.TrackingRepository()
    }
    
    @Singleton
    @Provides
    fun provideProfileRepository(helperApi: HelperApi): ProfileRepository {
        return ProfileRepository(helperApi)
    }

    @Singleton
    @Provides
    fun provideAssistanceRequestRepository(helperApi: HelperApi): AssistanceRequestRepository {
        return AssistanceRequestRepository(helperApi)
    }
}