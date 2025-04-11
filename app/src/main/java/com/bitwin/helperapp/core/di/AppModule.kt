package com.bitwin.helperapp.core.di

import com.bitwin.helperapp.features.register.data.RegisterApi
import com.bitwin.helperapp.features.register.domain.RegisterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api-gateway.test.bitwin.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideRegisterApi(retrofit: Retrofit): RegisterApi {
        return retrofit.create(RegisterApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRegisterRepository(registerApi: RegisterApi): RegisterRepository {
        return RegisterRepository(registerApi)
    }
}