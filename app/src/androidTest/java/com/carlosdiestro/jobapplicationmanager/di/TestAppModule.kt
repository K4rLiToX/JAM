package com.carlosdiestro.jobapplicationmanager.di

import android.content.Context
import androidx.room.Room
import com.carlosdiestro.jobapplicationmanager.datasource.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDB(
        @ApplicationContext ctx: Context
    ) = Room.inMemoryDatabaseBuilder(ctx, LocalDatabase::class.java)
        .allowMainThreadQueries()
        .build()
}