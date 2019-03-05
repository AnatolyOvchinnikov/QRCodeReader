package com.app.qrcodeapplication.model.data.db

import android.arch.persistence.room.*
import com.app.qrcodeapplication.entity.Check
import io.reactivex.Single

@Dao
interface CheckDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(check: Check)

    @Query("SELECT * FROM `check` ORDER BY scanTimestamp DESC")
    fun getChecksList(): Single<List<Check>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(check: Check): Int
}