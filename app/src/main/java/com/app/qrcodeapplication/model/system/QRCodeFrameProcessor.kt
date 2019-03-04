package com.app.qrcodeapplication.model.system

import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import android.util.Size
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader
import com.jakewharton.rxrelay2.PublishRelay
import io.fotoapparat.preview.Frame
import io.fotoapparat.preview.FrameProcessor
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class QRCodeFrameProcessor(
) : FrameProcessor {
    var previewRect: Rect = Rect()
    var previewSize: Size = Size(0, 0)
    var barcodeImageSize: Size = Size(0, 0)

    var enabled: Boolean = false
        set(value) {
            field = value
            triesCount.set(0)
        }

    private val qrcodeReader = QRCodeReader()
    private var data: ByteArray? = null
    private val hints: EnumMap<DecodeHintType, Any>
    private var triesCount: AtomicInteger = AtomicInteger(0)

    private val QRCodeResultRelay: PublishRelay<QRCodeResult> = PublishRelay.create()
    val observable: Observable<QRCodeResult> = QRCodeResultRelay

    init {
        hints = EnumMap<DecodeHintType, Any>(DecodeHintType::class.java)
        hints.put(DecodeHintType.TRY_HARDER, true)
        hints.put(DecodeHintType.ALLOWED_LENGTHS,
            VALID_BARCODE_LENGTHS
        )
    }

    companion object {
        private val VALID_BARCODE_REGEX = Regex(".*fn.*&.*fp.*")
        private var VALID_BARCODE_LENGTHS = intArrayOf(22)
    }

    override fun process(frame: Frame) {
//        if (enabled && !(previewRect.isEmpty || previewSize.isEmpty)) {
            val width = frame.size.height
            val height = frame.size.width

            val portraitData = rotateToPortrait(frame)
            val rect = detectionRect(width, height)
            val barcode = detectBarcode(portraitData, width, height, rect)

            if (barcode != null && VALID_BARCODE_REGEX.matches(barcode.text)) {
                val barcodePosition: Point? = barcodePosition(barcode)
//                val barcodeImage: Bitmap? = barcode.text.barcodeImage(barcodeImageSize)
                QRCodeResultRelay.accept(
                    QRCodeResult.Success(
                        barcode.text,
                        barcodePosition
                    )
                )
            }
//        }
    }

    private fun rotateToPortrait(frame: Frame): ByteArray? {
        if (data == null || data?.size != frame.image.size) {
            data = ByteArray(frame.image.size)
        }
        val width = frame.size.width
        val height = frame.size.height
        data?.let { data ->
            // only Y channel required
            for (y in 0 until height) {
                for (x in 0 until width)
                    data[x * height + height - y - 1] = frame.image[x + y * width]
            }
        }
        return data
    }

    private fun detectBarcode(data: ByteArray?, width: Int, height: Int, rect: Rect): Result? {
        var result: Result? = null
        val source = PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        try {
            result = qrcodeReader.decode(bitmap, hints)
            Log.v("QR_TEST", result?.toString())
        } catch (re: ReaderException) {
            // do nothing
        } finally {
            qrcodeReader.reset()
        }
        return result
    }

    private fun barcodePosition(barcode: Result): Point? {
        var position: Point? = null
        barcode.resultPoints.firstOrNull()?.apply {
            position = Point(x.toInt(), y.toInt())
        }
        return position
    }

    private fun detectionRect(cameraWidth: Int, cameraHeight: Int): Rect {
        return Rect(
                (previewRect.left.toFloat() * cameraWidth / previewSize.width).toInt(),
                (previewRect.top.toFloat() * cameraHeight / previewSize.height).toInt(),
                (previewRect.right.toFloat() * cameraWidth / previewSize.width).toInt(),
                (previewRect.bottom.toFloat() * cameraHeight / previewSize.height).toInt()
        )
    }
}