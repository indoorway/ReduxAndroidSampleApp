package com.indoorway.reduxandroidsampleapp.common

import com.indoorway.reduxandroidsampleapp.main.MainState

data class AppState(val authorized: Authorized? = null) {
    data class Authorized(
            val main: MainState
    )
}