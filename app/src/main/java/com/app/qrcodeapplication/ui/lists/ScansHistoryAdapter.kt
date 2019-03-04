package com.app.qrcodeapplication.ui.lists

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.app.qrcodeapplication.entity.Check


class ScansHistoryAdapter(var scansHistoryList: List<Check>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val viewTypes = arrayListOf(ScanViewHolder::class.java, EmptyViewHolder::class.java)

    override fun getItemViewType(position: Int): Int {
        return if(scansHistoryList.isEmpty()) {
            viewTypes.indexOf(EmptyViewHolder::class.java)
        } else {
            viewTypes.indexOf(ScanViewHolder::class.java)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val aClass = viewTypes.get(p1)
        return if(aClass.equals(EmptyViewHolder::class.java)) {
            EmptyViewHolder.create(p0)
        } else {
            ScanViewHolder.create(p0)
        }
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if(p0 is ScanViewHolder) {
            val scanItem = scansHistoryList.get(p1)
            if (scanItem != null) {
                p0.bind(scanItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return if(scansHistoryList.isEmpty()) {
            1
        } else {
            scansHistoryList.size
        }
    }

    fun updateData(scansHistoryList: List<Check>) {
        if(this.scansHistoryList.isEmpty()) {
            notifyDataSetChanged()
        } else {
            val scansDiffUtilCallback = ScansDiffUtilCallback(this.scansHistoryList, scansHistoryList)
            val productDiffResult = DiffUtil.calculateDiff(scansDiffUtilCallback)
            productDiffResult.dispatchUpdatesTo(this)
        }
        this.scansHistoryList = scansHistoryList
    }
}