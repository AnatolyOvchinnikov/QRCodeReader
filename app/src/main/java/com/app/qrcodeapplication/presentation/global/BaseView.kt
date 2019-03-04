package com.app.qrcodeapplication.presentation.global

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface BaseView : MvpView {
    fun showProgress(progress: Boolean)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showError(error: Throwable)
}