package kr.toru.lmwnassignment.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kr.toru.lmwnassignment.network.ApiService
import kr.toru.lmwnassignment.network.httpClientAndroid
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideApiService(httpClient: HttpClient): ApiService {
        return ApiService(httpClient)
    }

    @Provides
    fun provideHttpClient(): HttpClient {
        return httpClientAndroid
    }
}