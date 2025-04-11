package com.bitwin.helperapp.core.di
import com.bitwin.helperapp.core.api.HelperApi
import com.bitwin.helperapp.features.register.domain.RegisterRepository
import com.bitwin.helperapp.features.login.domain.LoginRepository
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

}