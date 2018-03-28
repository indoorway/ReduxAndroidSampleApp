package com.indoorway.reduxandroidsampleapp.main

import com.indoorway.reduxandroidsampleapp.model.GithubResponse

data class MainState(
        val githubData: GithubResponse?,
        val showLoader: Boolean,
        val showError: Boolean)
