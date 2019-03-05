package com.app.qrcodeapplication.ui.global

import android.content.Context
import android.support.v7.app.AlertDialog
import android.webkit.WebView
import com.app.qrcodeapplication.R
import com.app.qrcodeapplication.entity.Check

fun showCheckData(context: Context, check: Check, dialogDismissed: (() -> Unit?)? = null) {
    val alertDialog = AlertDialog.Builder(context)
    if(check.checkDataHtml != null && check.checkDataHtml?.isNotBlank() == true) {
        val webview = WebView(context)
        webview.apply {
            settings.javaScriptEnabled = true
            loadData(check.checkDataHtml, "text/html", "UTF-8");
        }
        alertDialog.setView(webview)
    } else {
        alertDialog.setMessage(R.string.data_error)
    }

    alertDialog.setPositiveButton(context.getString(R.string.ok), { dialog, which ->
        dialog.dismiss()
        dialogDismissed?.let { it() }
    })
        .create().show()
}