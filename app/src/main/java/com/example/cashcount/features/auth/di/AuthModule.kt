package com.example.cashcount.features.auth.di

import com.example.cashcount.features.auth.data.local.AuthLocalDataSource
import com.example.cashcount.features.auth.data.local.IAuthLocalDataSource
import com.example.cashcount.features.auth.data.repository.AuthRepository
import com.example.cashcount.features.auth.data.repository.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object AuthModule {

    @Provides
    @ActivityRetainedScoped
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @ActivityRetainedScoped
    fun provideAuthLocalDataSource(
        impl: AuthLocalDataSource
    ): IAuthLocalDataSource = impl

    @Provides
    @ActivityRetainedScoped
    fun provideAuthRepository(
        impl: AuthRepository
    ): IAuthRepository = impl

}