package com.indoorway.reduxandroidsampleapp.main

import com.indoorway.reduxandroidsampleapp.common.AppEvent

sealed class MainEvent : AppEvent {
    object OnCreate : MainEvent()
}