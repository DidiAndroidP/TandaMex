package com.didiermendoza.tandamex.src.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.didiermendoza.tandamex.src.core.database.dao.*
import com.didiermendoza.tandamex.src.core.database.entities.*

@Database(
    entities = [
        TandaEntity::class,
        UserEntity::class,
        TandaDetailEntity::class,
        TandaMemberEntity::class,
        ScheduleSummaryEntity::class,
        TurnoEntity::class,
        WalletEntity::class,
        TandaPaymentEntity::class
    ],
    version = 4, // Subimos la versión
    exportSchema = false
)
abstract class TandaMexDatabase : RoomDatabase() {
    abstract fun tandaDao(): TandaDao
    abstract fun userDao(): UserDao
    abstract fun tandaDetailDao(): TandaDetailDao
    abstract fun tandaMemberDao(): TandaMemberDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun walletDao(): WalletDao
    abstract fun tandaPaymentDao(): TandaPaymentDao
}