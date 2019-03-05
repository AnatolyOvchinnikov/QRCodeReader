package com.app.qrcodeapplication.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "check")
@Parcelize
data class Check(
        @PrimaryKey var scanTimestamp: Long,
        val fiscalNumber: String,
        val fiscalSign: String,
        var fiscalDocument: String,
        var date: String,
        var sum: String,
        var checkDataHtml: String? = null
) : Parcelable