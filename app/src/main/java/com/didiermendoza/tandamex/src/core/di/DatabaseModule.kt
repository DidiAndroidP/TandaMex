package com.didiermendoza.tandamex.src.core.di

import android.content.Context
import androidx.room.Room
import com.didiermendoza.tandamex.src.core.database.TandaMexDatabase
import com.didiermendoza.tandamex.src.core.database.dao.ScheduleDao
import com.didiermendoza.tandamex.src.core.database.dao.TandaDao
import com.didiermendoza.tandamex.src.core.database.dao.TandaDetailDao
import com.didiermendoza.tandamex.src.core.database.dao.TandaMemberDao
import com.didiermendoza.tandamex.src.core.database.dao.TandaPaymentDao
import com.didiermendoza.tandamex.src.core.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TandaMexDatabase {
        return Room.databaseBuilder(
            context,
            TandaMexDatabase::class.java,
            "TandaMexDB"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTandaDao(db: TandaMexDatabase): TandaDao = db.tandaDao()

    @Provides
    fun provideUserDao(db: TandaMexDatabase): UserDao = db.userDao()

    @Provides
    fun provideTandaDetailDao(db: TandaMexDatabase): TandaDetailDao = db.tandaDetailDao()

    @Provides
    fun provideTandaMemberDao(db: TandaMexDatabase): TandaMemberDao = db.tandaMemberDao()

    @Provides
    fun provideScheduleDao(db: TandaMexDatabase): ScheduleDao = db.scheduleDao()

    @Provides
    fun provideTandaPaymentDao(db: TandaMexDatabase): TandaPaymentDao = db.tandaPaymentDao()
}