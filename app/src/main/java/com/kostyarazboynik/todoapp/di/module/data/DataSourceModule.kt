package com.kostyarazboynik.todoapp.di.module.data

import android.content.Context
import com.kostyarazboynik.todoapp.data.datasource.local.SharedPreferencesAppSettings
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
object DataSourceModule {

    @Reusable
    @Provides
    fun provideSharedPreferencesDataSource(context: Context): SharedPreferencesAppSettings =
        SharedPreferencesAppSettings(context)
}
