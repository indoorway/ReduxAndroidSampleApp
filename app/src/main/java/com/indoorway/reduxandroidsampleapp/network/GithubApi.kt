package com.indoorway.reduxandroidsampleapp.network

import com.indoorway.reduxandroidsampleapp.model.GithubResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface GithubApi {

    @GET("orgs/{user}/repos")
    fun getUserData(@Path("user") user: String): Single<List<GithubResponse>>
}
