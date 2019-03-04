package com.app.qrcodeapplication.ui.lists

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.qrcodeapplication.R
import com.app.qrcodeapplication.databinding.ItemScanLayoutBinding
import com.app.qrcodeapplication.entity.Check
import java.text.SimpleDateFormat
import java.util.*

class ScanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private lateinit var check: Check

    var checkObservable = ObservableField<StringBuilder>()
    val timestampObservable = ObservableField<String>()

    val scanText = StringBuilder()

    init {
        view.setOnClickListener {
            AlertDialog.Builder(view.context)
                .setMessage(scanText)
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                .create().show()
        }
    }

    fun bind(check: Check) {
        if (check != null) {
//            checkObservable.set(check)
            this.check = check
            val resources = itemView.resources

            scanText.append("fiscalNumber: ").append(check.fiscalNumber).append(System.lineSeparator())
            scanText.append("fiscalSign: ").append(check.fiscalSign).append(System.lineSeparator())
            scanText.append("fiscalDocument: ").append(check.fiscalDocument).append(System.lineSeparator())
            scanText.append("date: ").append(check.date).append(System.lineSeparator())
            scanText.append("sum: ").append(check.sum).append(System.lineSeparator())

            val fDate = SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(check.scanTimestamp * 1000L))
            timestampObservable.set(fDate)

            checkObservable.set(scanText)
//            itemView.user_name.text = user.user.name
        } else {

        }
    }

    companion object {
        fun create(parent: ViewGroup): ScanViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scan_layout, parent, false)

            val binding = DataBindingUtil.bind<ItemScanLayoutBinding>(view)
            val userViewHolder = ScanViewHolder(view)
            binding?.viewholder = userViewHolder

            return binding?.viewholder!!
//            return UserViewHolder(view)
        }
    }
}