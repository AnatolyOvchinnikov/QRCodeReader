package com.app.qrcodeapplication.presentation.scan

import com.app.qrcodeapplication.entity.Check
import com.app.qrcodeapplication.presentation.global.BaseView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ScanCodeView : BaseView {
    fun showMessage(check: Check)
}
