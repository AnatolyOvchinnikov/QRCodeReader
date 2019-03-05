package com.app.qrcodeapplication.ui.lists

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.qrcodeapplication.R
import com.app.qrcodeapplication.databinding.ItemScanLayoutBinding
import com.app.qrcodeapplication.entity.Check
import com.app.qrcodeapplication.ui.global.showCheckData
import java.text.SimpleDateFormat
import java.util.*

class ScanViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private lateinit var check: Check

    var checkObservable = ObservableField<StringBuilder>()
    val timestampObservable = ObservableField<String>()

    init {
        view.setOnClickListener {
            showCheckData(view.context, check)
        }
    }

    fun bind(check: Check) {
        if (check != null) {
            this.check = check
            val resources = itemView.resources

            val scanText = StringBuilder()

            scanText.append(view.context.getString(R.string.fiscal_number))
                .append(check.fiscalNumber)
                .append(System.lineSeparator())
            scanText.append(view.context.getString(R.string.fiscal_sign))
                .append(check.fiscalSign)
                .append(System.lineSeparator())
            scanText.append(view.context.getString(R.string.fiscal_document))
                .append(check.fiscalDocument)
                .append(System.lineSeparator())
            scanText.append(view.context.getString(R.string.check_date))
                .append(check.date)
                .append(System.lineSeparator())
            scanText.append(view.context.getString(R.string.check_sum))
                .append(check.sum)
                .append(System.lineSeparator())

            val fDate = SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(check.scanTimestamp * 1000L))
            timestampObservable.set(fDate)

            checkObservable.set(scanText)
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
        }
    }
}