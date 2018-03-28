package com.indoorway.reduxandroidsampleapp.common.di

import com.indoorway.reduxandroidsampleapp.common.AppEvent
import com.indoorway.reduxandroidsampleapp.common.AppState
import com.indoorway.reduxandroidsampleapp.common.GeneralSubReducer
import com.indoorway.reduxandroidsampleapp.common.GlobalModel
import com.indoorway.reduxandroidsampleapp.main.MainModel
import com.indoorway.reduxandroidsampleapp.main.MainState
import com.indoorway.reduxandroidsampleapp.network.GithubApi
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GlobalModelModuleImpl {

    val retrofit = Retrofit
            .Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    val globalModel: GlobalModel =
            GlobalModel(
                    initialState,
                    mainReducer()
            )

    private fun mainReducer(): GeneralSubReducer<MainState> =
            { _: Relay<AppEvent>, events: Observable<AppEvent>, states: Observable<MainState> ->
                MainModel(
                        states = states,
                        uiScheduler = AndroidSchedulers.mainThread(),
                        ioScheduler = Schedulers.io(),
                        api = retrofit.create(GithubApi::class.java)
                ).process(events)
            }

    companion object {

        //for simplicity instead of proper DI
        var initialState: AppState = GlobalModel.initialAuthorized()
        val globalModel = GlobalModelModuleImpl().globalModel
    }

}