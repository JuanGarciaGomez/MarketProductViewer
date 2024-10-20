package com.juanfe.project.marketproductviewer.data.repository.di

import com.juanfe.project.marketproductviewer.data.repository.SearchProductRepositoryImpl
import com.juanfe.project.marketproductviewer.domain.SearchProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindsCatBreedRepository(catBreedRepositoryImpl: SearchProductRepositoryImpl): SearchProductRepository


}