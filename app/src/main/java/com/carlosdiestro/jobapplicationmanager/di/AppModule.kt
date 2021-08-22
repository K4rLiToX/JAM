package com.carlosdiestro.jobapplicationmanager.di

import android.content.Context
import androidx.room.Room
import com.carlosdiestro.jobapplicationmanager.datasource.LocalDatabase
import com.carlosdiestro.jobapplicationmanager.datasource.dao.JobApplicationDAO
import com.carlosdiestro.jobapplicationmanager.datasource.repositories.MainRepository
import com.carlosdiestro.jobapplicationmanager.interfaces.IMainRepository
import com.carlosdiestro.jobapplicationmanager.utils.Constants.LOCALDATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLocalDatabase(
        @ApplicationContext ctx: Context
    ) = Room.databaseBuilder(
        ctx,
        LocalDatabase::class.java,
        LOCALDATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideJobApplicationDAO(db: LocalDatabase) = db.getJobApplicationDAO()

    @Singleton
    @Provides
    fun provideMainRepository(
        dao: JobApplicationDAO
    ) = MainRepository(dao) as IMainRepository
}