package com.example.dscanner.data.di

import android.content.Context
import androidx.room.Room
import com.example.dscanner.data.local.ProductDataBase
import com.example.dscanner.data.local.dao.CompetitorsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object RoomModule {

    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): ProductDataBase {
        return Room.databaseBuilder(context, ProductDataBase::class.java, "ProductDataBase")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun competitorsDao(dataBase: ProductDataBase) : CompetitorsDao {
        return dataBase.competitorsDao()
    }

}