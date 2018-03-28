package com.indoorway.reduxandroidsampleapp.common.utils

import com.indoorway.reduxandroidsampleapp.common.AppEvent
import com.indoorway.reduxandroidsampleapp.common.di.GlobalModelModuleImpl
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

fun <State> RxAppCompatActivity.bind(events: Observable<AppEvent>,
                                     extractPartOfState: () -> Observable<State>,
                                     handleStateStream: (Observable<State>) -> Unit = { it.subscribe() }) {
    internalBind(
            lifecycleProvider = this,
            events = events,
            extractPartOfState = extractPartOfState,
            handleStateStream = handleStateStream
    )
}

private fun <State> internalBind(lifecycleProvider: LifecycleProvider<*>,
                                 events: Observable<AppEvent>,
                                 extractPartOfState: () -> Observable<State>,
                                 handleStateStream: (Observable<State>) -> Unit) {
    events
            .bindToLifecycle(lifecycleProvider)
            .subscribe(GlobalModelModuleImpl.globalModel.events)

    handleStateStream(
            extractPartOfState()
                    .observeOn(AndroidSchedulers.mainThread())
                    .bindToLifecycle(lifecycleProvider))
}