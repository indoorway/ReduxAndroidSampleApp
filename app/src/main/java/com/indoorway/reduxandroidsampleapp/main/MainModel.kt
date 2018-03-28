package com.indoorway.reduxandroidsampleapp.main

import android.util.Log
import com.indoorway.reduxandroidsampleapp.common.AppEvent
import com.indoorway.reduxandroidsampleapp.common.GeneralSubModel
import com.indoorway.reduxandroidsampleapp.network.GithubApi
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.withLatestFrom

class MainModel(states: Observable<MainState>,
                private val ioScheduler: Scheduler,
                private val uiScheduler: Scheduler,
                private val api: GithubApi
) : GeneralSubModel<MainState>(states) {

    override fun process(events: Observable<AppEvent>): Observable<MainState> {
        return Observable.merge(listOf(callApiOnCreate(events)))
    }

    private fun callApiOnCreate(events: Observable<AppEvent>): Observable<MainState> {
        return events.ofType(MainEvent.OnCreate::class.java)
                .withLatestFrom(states, { event, state -> event to state })
                .filter { (_, state) -> state.githubData == null }
                .flatMap { (event, state) ->
                    api.getUserData("octokit")
                            .subscribeOn(ioScheduler)
                            .toObservable()
                            .onLatestFrom(states) {
                                copy(githubData = it.first(), showLoader = false, showError = false)
                            }
                            .observeOn(uiScheduler)
                            .startWith(state.copy(showLoader = true, showError = false))
                            .doOnError { Log.e(javaClass.simpleName, it.message, it) }
                            .onErrorReturn { states.blockingFirst().copy(showError = true, showLoader = false) }
                }
    }

    companion object {
        fun initialState(): MainState {
            return MainState(
                    showError = false,
                    showLoader = false,
                    githubData = null)
        }
    }

    fun <T, R> Observable<T>.onLatestFrom(other: Observable<R>, action: R.(T) -> R): Observable<R> {
        return withLatestFrom(other, BiFunction { t1, t2 ->
            t2.action(t1)
        })
    }
}