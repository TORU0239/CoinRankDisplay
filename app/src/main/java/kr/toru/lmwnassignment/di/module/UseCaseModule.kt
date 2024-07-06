package kr.toru.lmwnassignment.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kr.toru.lmwnassignment.data.usecase.GetCoinsUseCase
import kr.toru.lmwnassignment.data.usecase.impl.GetCoinsUseCaseImpl
import kr.toru.lmwnassignment.network.ApiService

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideGetCoinsUseCase(apiService: ApiService): GetCoinsUseCase {
        return GetCoinsUseCaseImpl(apiService = apiService)
    }
}