package com.app.qrcodeapplication.ui.lists

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.qrcodeapplication.R
import com.app.qrcodeapplication.entity.Check

class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private lateinit var check: Check

    companion object {
        fun create(parent: ViewGroup): EmptyViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scan_empty_layout, parent, false)
            return EmptyViewHolder(view)
        }
    }
}