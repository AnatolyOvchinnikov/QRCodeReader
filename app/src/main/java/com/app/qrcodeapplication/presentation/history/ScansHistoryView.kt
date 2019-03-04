package com.app.qrcodeapplication.presentation.history

import com.app.qrcodeapplication.entity.Check
import com.app.qrcodeapplication.presentation.global.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ScansHistoryView : BaseView {
    fun loadItems(checkList: List<Check>)
}
