package com.app.qrcodeapplication.model.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.app.qrcodeapplication.entity.Check

@Database(
    entities = [Check::class],
    version = 1,
    exportSchema = false
)
abstract class QRCodeDatabase : RoomDatabase() {
    abstract fun checkDao(): CheckDao
}