package kr.toru.lmwnassignment.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kr.toru.lmwnassignment.data.usecase.GetCoinDetailUseCase
import kr.toru.lmwnassignment.data.usecase.GetCoinsUseCase
import kr.toru.lmwnassignment.data.usecase.SearchCoinsUseCase
import kr.toru.lmwnassignment.data.usecase.impl.GetCoinDetailUseCaseImpl
import kr.toru.lmwnassignment.data.usecase.impl.GetCoinsUseCaseImpl
import kr.toru.lmwnassignment.data.usecase.impl.SearchCoinsUseCaseImpl
import kr.toru.lmwnassignment.network.ApiService

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideGetCoinsUseCase(apiService: ApiService): GetCoinsUseCase {
        return GetCoinsUseCaseImpl(apiService = apiService)
    }

    @Provides
    @ViewModelScoped
    fun provideGetCoinDetailUseCase(apiService: ApiService): GetCoinDetailUseCase {
        return GetCoinDetailUseCaseImpl(apiService = apiService)
    }

    @Provides
    @ViewModelScoped
    fun provideSearchCoinsUseCase(apiService: ApiService): SearchCoinsUseCase {
        return SearchCoinsUseCaseImpl(apiService = apiService)
    }
}