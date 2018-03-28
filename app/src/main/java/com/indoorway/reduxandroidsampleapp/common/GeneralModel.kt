package com.indoorway.reduxandroidsampleapp.common

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

abstract class GeneralModel<UiEvent, UiState>(defaultState: UiState) {
    val events = PublishRelay.create<UiEvent>()
    val states = BehaviorRelay.createDefault<UiState>(defaultState)
}

abstract class GeneralSubModel<UiState>(val states: Observable<UiState>) {

    abstract fun process(events: Observable<AppEvent>): Observable<UiState>
}

typealias GeneralSubReducer<UiState> = (eventsRelay: Relay<AppEvent>, events: Observable<AppEvent>, states: Observable<UiState>) -> Observable<UiState>
