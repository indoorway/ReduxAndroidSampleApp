package com.indoorway.reduxandroidsampleapp.common

import com.indoorway.reduxandroidsampleapp.common.utils.mapNotNull
import com.indoorway.reduxandroidsampleapp.common.utils.onLatestFrom
import com.indoorway.reduxandroidsampleapp.main.MainModel
import com.indoorway.reduxandroidsampleapp.main.MainState
import io.reactivex.Observable

class GlobalModel(initialState: AppState,
                  private val mainReducer: GeneralSubReducer<MainState>
) : GeneralModel<AppEvent, AppState>(initialState), CommonGlobalModel {

    override val mainState: Observable<MainState> = states.mapNotNull { it.authorized?.main }

    init {
        Observable.merge(listOf(
                main()
        )).subscribe(states)
    }

    private fun main() =
            mainReducer(events, events, mainState)
                    .onLatestFrom(states) { copy(authorized = authorized?.copy(main = it)) }

    companion object {
        fun initialAuthorized(): AppState {
            return AppState(
                    authorized = initialAuthorizedState())
        }

        private fun initialAuthorizedState(): AppState.Authorized {
            return AppState.Authorized(
                    main = MainModel.initialState()
            )
        }
    }

}