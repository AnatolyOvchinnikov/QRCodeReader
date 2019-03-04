package com.app.qrcodeapplication.ui.lists

import android.support.v7.util.DiffUtil
import com.app.qrcodeapplication.entity.Check

class ScansDiffUtilCallback(val oldList: List<Check>, val newList: List<Check>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.get(oldItemPosition).scanTimestamp == newList.get(newItemPosition).scanTimestamp
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.get(oldItemPosition).fiscalNumber == newList.get(newItemPosition).fiscalNumber
    }
}