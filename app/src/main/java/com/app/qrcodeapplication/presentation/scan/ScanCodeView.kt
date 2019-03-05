package com.app.qrcodeapplication.presentation.scan

import com.app.qrcodeapplication.entity.Check
import com.app.qrcodeapplication.presentation.global.BaseView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface ScanCodeView : BaseView {
    fun showMessage(check: Check)
}
