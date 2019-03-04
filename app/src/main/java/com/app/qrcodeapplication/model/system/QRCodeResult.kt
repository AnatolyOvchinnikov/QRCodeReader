package com.app.qrcodeapplication.model.system

import android.graphics.Point

sealed class QRCodeResult {

    data class Success(val code: String, val position: Point?/*, val image: Bitmap?*/) : QRCodeResult()

    object Backside : QRCodeResult()

    object NotReadable : QRCodeResult()
}