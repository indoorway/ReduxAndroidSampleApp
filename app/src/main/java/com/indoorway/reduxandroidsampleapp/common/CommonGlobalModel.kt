package com.indoorway.reduxandroidsampleapp.common

import com.indoorway.reduxandroidsampleapp.main.MainState
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

interface CommonGlobalModel {
    val events: PublishRelay<AppEvent>
    val mainState: Observable<MainState>
}